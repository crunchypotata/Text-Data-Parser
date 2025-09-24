package com.example.sigmabank.parse;

import com.example.sigmabank.io.ErrorLogger;
import com.example.sigmabank.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class LineParser {
    private final ErrorLogger errorLogger;
    public LineParser(ErrorLogger errorLogger){ this.errorLogger = errorLogger; }

    public void parseFile(Path file,
                          Map<Integer, Manager> managersById,
                          Map<Integer, Employee> employeesById,
                          List<Employee> employeesReadOrder) {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)){
            String line;
            while ((line = br.readLine()) != null){
                String raw = line;
                line = line.trim();
                if (line.isEmpty())
                    continue;
                try {
                    String[] parts = splitCsv(line);
                    if (parts.length < 5){
                        errorLogger.log(raw, "Not enough fields (expected 5)");
                        continue;
                    }
                    String role = parts[0].trim();
                    String idStr = parts[1].trim();
                    String name = parts[2].trim();
                    String salaryStr = parts[3].trim();
                    String last = parts[4].trim();

                    Integer id = parseIntSafe(idStr);
                    if (id == null) { errorLogger.log(raw, "Incorrect id");
                        continue; }
                    if (name.isEmpty()) { errorLogger.log(raw, "Empty name field");
                        continue; }
                    Double salary = parseSalaryOrNull(salaryStr);

                    if ("Manager".equalsIgnoreCase(role)){
                        if (last.isEmpty()) { errorLogger.log(raw, "Empty department by manager");
                            continue; }
                        Manager m = new Manager(id, name, salary, last);
                        Manager prev = managersById.putIfAbsent(id, m);
                        if (prev != null) errorLogger.log(raw, "Dublicate manager id : " + id);
                    } else if ("Employee".equalsIgnoreCase(role)){
                        Integer managerId = parseIntSafe(last);
                        if (managerId == null) { errorLogger.log(raw, "Incorrect manager id of employee");
                            continue; }
                        Employee e = new Employee(id, name, salary, managerId);
                        Employee prev = employeesById.putIfAbsent(id, e);
                        if (prev != null) errorLogger.log(raw, "Dublicate employee id: " + id);
                        else employeesReadOrder.add(e);
                    } else {
                        errorLogger.log(raw, "Unknown role: " + role);
                    }
                } catch (Exception ex){
                    errorLogger.log(raw, "Exception while parsing: " + ex.getMessage());
                }
            }
        } catch (IOException io){
            errorLogger.log(file.toString(), "Failed to read file: " + io.getMessage());
        }
    }

    private static String[] splitCsv(String line){
        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }
    private static Integer parseIntSafe(String s){
        try { return Integer.parseInt(s); } catch (Exception e){ return null; }
    }
    private static Double parseSalaryOrNull(String s){
        if (s == null || s.isEmpty()) return null;
        try {
            double v = Double.parseDouble(s);
            if (v > 0) return v;
            return null;
        } catch (Exception e){ return null; }
    }
}
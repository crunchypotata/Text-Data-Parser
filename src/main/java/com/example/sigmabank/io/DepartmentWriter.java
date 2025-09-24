package com.example.sigmabank.io;

import com.example.sigmabank.model.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;

public final class DepartmentWriter {
    public void writeAll(Path dir, Map<String, Department> deps) {
        try { Files.createDirectories(dir); } catch (IOException ignore) {}
        for (Department d : deps.values()) {
            if (d.manager == null)
                continue;
            Path out = dir.resolve(d.name + ".sb");
            try (BufferedWriter bw = Files.newBufferedWriter(out, StandardCharsets.UTF_8)){
                bw.write(d.manager.toString());
                bw.newLine();
                for (Employee e : d.employeesInInputOrder){
                    bw.write(e.toString());
                    bw.newLine();
                }
            } catch (IOException e){
                System.err.println("Failed to write" + out + ": " + e.getMessage());
            }
        }
    }
}
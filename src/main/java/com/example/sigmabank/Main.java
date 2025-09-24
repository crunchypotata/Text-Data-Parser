package com.example.sigmabank;

import com.example.sigmabank.io.*;
import com.example.sigmabank.model.*;
import com.example.sigmabank.parse.LineParser;
import com.example.sigmabank.service.*;
import com.example.sigmabank.util.Args;
import java.nio.file.Files;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] argv) {
        Args args;
        try {
            args = Args.parse(argv);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка параметров: " + e.getMessage());
            System.err.println("Примеры: \n  --sort=name --order=asc --stat\n  -s=salary --order=desc --stat -o=file --path=output/stat.txt\n  --stat");
            return;
        }

        Path workDir = Paths.get("");

        Path outputDir;
        if (args.statEnabled
                && args.output == Args.Output.FILE
                && args.outputPath != null
                && args.outputPath.getParent() != null) {
            outputDir = args.outputPath.getParent();
        } else {
            outputDir = workDir.resolve("output");
        }

        try {
            Files.createDirectories(outputDir);
        } catch (IOException ioe) {
            System.err.println("Не удалось создать каталог вывода: " + outputDir.toAbsolutePath());
            System.err.println(ioe.getMessage());
            return;
        }

        List<Path> files = FileScanner.findSbFiles(workDir);
        if (files.isEmpty()) {
            System.out.println(".sb файлы не найдены в текущей директории");
            return;
        }

        try (var errorLogger = new ErrorLogger(outputDir.resolve("error.log"))) {

            var parser = new LineParser(errorLogger);

            Map<Integer, Manager> managersById = new HashMap<>();
            Map<Integer, Employee> employeesById = new HashMap<>();
            List<Employee> employeesReadOrder = new ArrayList<>();

            for (Path p : files) {
                parser.parseFile(p, managersById, employeesById, employeesReadOrder);
            }

            var deptService = new DepartmentService();
            Map<String, Department> departments = deptService.buildDepartments(managersById, employeesReadOrder);

            var sortService = new SortService();
            sortService.sortEmployees(departments, args);

            var deptWriter = new DepartmentWriter();
            deptWriter.writeAll(outputDir, departments);

            if (args.statEnabled) {
                var statsService = new StatsService();
                List<StatsService.Row> rows = statsService.compute(departments);
                var statWriter = new StatWriter();
                statWriter.write(rows, args);
            }

            System.out.println("Готово. Выходные файлы: " + outputDir.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
        }
    }
}

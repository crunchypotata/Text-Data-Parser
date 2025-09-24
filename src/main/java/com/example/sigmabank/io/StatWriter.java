package com.example.sigmabank.io;

import com.example.sigmabank.service.StatsService;
import com.example.sigmabank.util.Args;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public final class StatWriter {
    public void write(List<StatsService.Row> rows, Args args){
        if (args.output == Args.Output.CONSOLE){
            System.out.println("department, min, max, mid");
            for (var r : rows){
                System.out.println(r.department + ", " + r.min + ", " + r.max + ", " + r.avg);
            }
            return;
        }
        try {
            if (args.outputPath.getParent() != null) {
                Files.createDirectories(args.outputPath.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(args.outputPath, StandardCharsets.UTF_8)) {
                bw.write("department, min, max, mid\n");
                for (var r : rows) {
                    bw.write(r.department + ", " + r.min + ", " + r.max + ", " + r.avg + "\n");
                }
            }
        } catch (IOException e){
            System.err.println("Failed to write stattistic: " + e.getMessage());
        }
    }
}
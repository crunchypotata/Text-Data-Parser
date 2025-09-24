package com.example.sigmabank.util;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Args {
    public enum SortBy { NAME, SALARY }
    public enum Order { ASC, DESC }
    public enum Output { CONSOLE, FILE }

    public final SortBy sortBy;
    public final Order order;
    public final boolean statEnabled;
    public final Output output;
    public final Path outputPath;

    private Args(SortBy sortBy, Order order, boolean statEnabled, Output output, Path outputPath) {
        this.sortBy = sortBy;
        this.order = order;
        this.statEnabled = statEnabled;
        this.output = output;
        this.outputPath = outputPath;
    }

    public static Args parse(String[] argv) {
        Map<String,String> map = new HashMap<>();
        for (String a : argv) {
            String s = a.trim();
            if (s.isEmpty()) continue;
            if (s.startsWith("--")) {
                var eq = s.indexOf('=');
                if (eq > 0) map.put(s.substring(2, eq), s.substring(eq+1));
                else map.put(s.substring(2), "true");
            } else if (s.startsWith("-")) {
                var eq = s.indexOf('=');
                if (eq > 0) map.put(shortToLong(s.substring(1, eq)), s.substring(eq+1));
                else map.put(shortToLong(s.substring(1)), "true");
            } else {
                throw new IllegalArgumentException("Unknown parameter: " + s);
            }
        }

        boolean stat = map.containsKey("stat");

        Output output = Output.CONSOLE;
        if (map.containsKey("output")) {
            String v = map.get("output").toLowerCase();
            if (v.equals("console")) output = Output.CONSOLE;
            else if (v.equals("file")) output = Output.FILE;
            else throw new IllegalArgumentException("--output accepts: console, file");
        }

        java.nio.file.Path outPath = null;
        if (map.containsKey("path")) {
            if (output != Output.FILE) throw new IllegalArgumentException("--s only possible with --output=file");
            outPath = java.nio.file.Paths.get(map.get("path"));
        }
        if (output == Output.FILE && outPath == null) throw new IllegalArgumentException("--output=file allows --path=<путь>");

        SortBy sortBy = null; Order order = null;
        if (map.containsKey("sort")) {
            String v = map.get("sort").toLowerCase();
            if (v.equals("name")) sortBy = SortBy.NAME;
            else if (v.equals("salary")) sortBy = SortBy.SALARY;
            else throw new IllegalArgumentException("--sort accepts: name, salary");

            if (!map.containsKey("order")) {
                throw new IllegalArgumentException("--is required, if specified --sort");
            }
            String o = map.get("order").toLowerCase();
            if (o.equals("asc")) order = Order.ASC;
            else if (o.equals("desc")) order = Order.DESC;
            else throw new IllegalArgumentException("--order allows: asc, desc");
        } else if (map.containsKey("order")) {
            throw new IllegalArgumentException("--order cannot be asked without --sort");
        }

        return new Args(sortBy, order, stat, output, outPath);
    }

    private static String shortToLong(String s) {

        if (s.startsWith("s")) return "sort" + s.substring(1);
        if (s.startsWith("o")) return "output" + s.substring(1);
        return s;
    }
}
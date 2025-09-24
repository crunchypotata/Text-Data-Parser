package com.example.sigmabank.model;

public final class Manager extends Person {
    public final String departmentName;
    public Manager(int id, String name, Double salary, String departmentName) {
        super(id, name, salary);
        this.departmentName = departmentName;
    }
    @Override public String toString() {
        String sal = salary == null ? "" : toNumber(salary);
        return String.join(",", "Manager", String.valueOf(id), name, sal, departmentName);
    }
    private static String toNumber(Double d){
        String s = String.format(java.util.Locale.US, "%.2f", d);
        if (s.contains(".")) {
            s = s.replaceAll("0+$", "");
            if (s.endsWith(".")) s = s.substring(0, s.length()-1);
        }
        return s;
    }
}
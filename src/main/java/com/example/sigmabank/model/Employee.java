package com.example.sigmabank.model;

public final class Employee extends Person {
    public final Integer managerId;
    public Employee(int id, String name, Double salary, Integer managerId) {
        super(id, name, salary);
        this.managerId = managerId;
    }
    @Override public String toString() {
        String sal = salary == null ? "" : toNumber(salary);
        return String.join(",", "Employee", String.valueOf(id), name, sal, managerId == null ? "" : String.valueOf(managerId));
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
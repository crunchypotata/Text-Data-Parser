package com.example.sigmabank.model;

import java.util.*;

public final class Department {
    public final String name;
    public Manager manager;
    public final List<Employee> employeesInInputOrder = new ArrayList<>();
    public Department(String name){ this.name = name; }
}
package com.example.sigmabank.service;

import com.example.sigmabank.model.Department;
import com.example.sigmabank.model.Employee;
import com.example.sigmabank.util.Args;

import java.util.*;

public final class SortService {
    public void sortEmployees(Map<String, Department> deps, Args args){
        if (args.sortBy == null)
            return;
        boolean asc = args.order == Args.Order.ASC;
        for (Department d : deps.values()){
            Comparator<Employee> cmp;
            switch (args.sortBy){
                case NAME -> cmp = Comparator.comparing(e -> e.name, String.CASE_INSENSITIVE_ORDER);
                case SALARY -> cmp = Comparator.comparing((Employee e) -> safeSalary(e)).thenComparing(e -> e.name, String.CASE_INSENSITIVE_ORDER);
                default -> throw new IllegalStateException("Unexpected value: " + args.sortBy);
            }
            if (!asc) cmp = cmp.reversed();
            d.employeesInInputOrder.sort(cmp);
        }
    }
    private static double safeSalary(Employee e){
        return e.salary == null ? Double.NEGATIVE_INFINITY : e.salary;
    }
}
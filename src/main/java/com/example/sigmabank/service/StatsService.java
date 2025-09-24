package com.example.sigmabank.service;

import com.example.sigmabank.model.Department;
import com.example.sigmabank.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class StatsService {
    public static final class Row {
        public final String department;
        public final String min;
        public final String max;
        public final String avg;
        public Row(String department, String min, String max, String avg){
            this.department = department; this.min = min; this.max = max; this.avg = avg;
        }
    }

    public List<Row> compute(Map<String, Department> deps){
        List<Row> rows = new ArrayList<>();
        for (Department d : deps.values()){
            List<Double> salaries = new ArrayList<>();
            for (Employee e : d.employeesInInputOrder){
                if (e.salary != null) salaries.add(e.salary);
            }
            if (salaries.isEmpty()){
                rows.add(new Row(d.name, format(0), format(0), format(0)));
                continue;
            }
            double min = salaries.stream().min(Double::compareTo).get();
            double max = salaries.stream().max(Double::compareTo).get();
            double avg = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            rows.add(new Row(d.name, format(min), format(max), format(avg)));
        }
        return rows;
    }

    private static String format(double v){

        return new BigDecimal(v).setScale(2, RoundingMode.UP).toPlainString();
    }
}
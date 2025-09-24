package com.example.sigmabank.service;

import com.example.sigmabank.model.*;

import java.util.*;

public final class DepartmentService {
    public Map<String, Department> buildDepartments(Map<Integer, Manager> managersById,
                                                    List<Employee> employeesInReadOrder){
        Map<String, Department> deps = new TreeMap<>();
        for (Manager m : managersById.values()){
            Department d = deps.computeIfAbsent(m.departmentName, Department::new);
            if (d.manager == null) d.manager = m;
        }
        for (Employee e : employeesInReadOrder){
            Manager mgr = managersById.get(e.managerId);
            if (mgr == null)
                continue;

            Department d = deps.computeIfAbsent(mgr.departmentName, Department::new);
            d.employeesInInputOrder.add(e);
        }
        return deps;
    }
}
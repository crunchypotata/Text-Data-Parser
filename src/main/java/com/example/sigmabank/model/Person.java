package com.example.sigmabank.model;

public abstract class Person {
    public final int id;
    public final String name;
    public final Double salary;
    protected Person(int id, String name, Double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
}
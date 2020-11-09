package com.example.trackcovid.common;

public class Country {

    public String name;
    public int id;
    public int total_cases;
    public int pop;

    public Country(){
        // Default constructor
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getTotal_cases() {
        return total_cases;
    }

    public int getPop() {
        return pop;
    }

    public Country(int id, String name, int population, int total_cases) {
        this.name = name;
        this.id = id;
        this.pop = pop;
        this.total_cases = total_cases;
    }
}

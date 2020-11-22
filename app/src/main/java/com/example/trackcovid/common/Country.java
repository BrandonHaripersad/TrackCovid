package com.example.trackcovid.common;

public class Country {

    public String name;
    public int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Country(int id, String name) {
        this.name = name;
        this.id = id;
    }
}

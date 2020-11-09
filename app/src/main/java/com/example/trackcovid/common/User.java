package com.example.trackcovid.common;

public class User {

    public int id;
    public String first;
    public String last;

    public User(){
        // Default constructor
    }

    public User(int id, String first, String last) {
        this.id = id;
        this.first = first;
        this.last = last;
    }


}

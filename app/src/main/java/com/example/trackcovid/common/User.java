package com.example.trackcovid.common;

public class User {

    public String email;
    public String country;
    public String adminArea;

    public User(){
        // Default constructor
    }

    public User(String email){
        this.email = email;
        this.country = null;
        this.adminArea = null;
    }

    public User(String email, String country, String adminArea) {
        this.email = email;
    }


}

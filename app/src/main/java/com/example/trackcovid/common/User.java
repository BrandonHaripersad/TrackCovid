package com.example.trackcovid.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey @NonNull
    public String id;
    @ColumnInfo(name = "email")
    public String email;

    public User(String email, String id) {
        this.email = email;
        this.id = id;
    }


}
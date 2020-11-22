package com.example.trackcovid.common;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Location.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract LocationDao locationDao();
}

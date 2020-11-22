package com.example.trackcovid.common;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Update
    public void updateUser(User user);

    @Query("SELECT email from users where id = :id")
    public String getFirstName(int id);
}

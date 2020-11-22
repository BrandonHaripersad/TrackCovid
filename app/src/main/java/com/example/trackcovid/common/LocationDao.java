package com.example.trackcovid.common;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(Location location);

    @Update
    void updateLocation(Location location);

    @Query("SELECT country from location where user_id = :id ORDER by loc_id DESC LIMIT 1")
    public String getCountryName(String id);

    @Query("SELECT admin_area from location where user_id = :id ORDER by loc_id DESC LIMIT 1")
    public String getAdminArea(String id);

    @Query("SELECT longitude from location where user_id = :id")
    public double getLongitude(String id);

    @Query("SELECT latitude from location where user_id = :id")
    public double getLatitude(String id);

}

package com.example.trackcovid.common;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "location")
public class Location {
    @PrimaryKey (autoGenerate = true)
    public int loc_id;
    @ColumnInfo(name = "country")
    public String country;
    @ColumnInfo(name = "admin_area")
    public String admin_area;
    @ColumnInfo(name = "longitude")
    public double longitude;
    @ColumnInfo(name = "latitude")
    public double latitude;
    @ColumnInfo(name = "user_id")
    public String user_id;

    public Location(String country, String admin_area, double longitude, double latitude, String user_id){
        this.country = country;
        this.admin_area = admin_area;
        this.longitude = longitude;
        this.latitude = latitude;
        this.user_id = user_id;
    }

    public Location(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

}

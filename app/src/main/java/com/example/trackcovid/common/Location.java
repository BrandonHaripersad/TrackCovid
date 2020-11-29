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

    public int getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(int loc_id) {
        this.loc_id = loc_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(String admin_area) {
        this.admin_area = admin_area;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

package com.example.trackcovid.common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class LocationTest {

    private Location mLocation;

    @Before
    public void setUp() {
        mLocation = new Location("Canada", "Ontario", 51.2538, 85.3232, "10000000");
    }

    @Test
    public void getCountry() {
        String country = mLocation.getCountry();
        assertThat(country, is(equalTo("Canada")));
    }

    @Test
    public void getAdmin_area() {
        String country = mLocation.getAdmin_area();
        assertThat(country, is(equalTo("Ontario")));
    }

    @Test
    public void getLongitude() {
        Double country = mLocation.getLongitude();
        assertThat(country, is(equalTo(51.2538)));
    }

    @Test
    public void getLatitude() {
        Double country = mLocation.getLatitude();
        assertThat(country, is(equalTo(85.3232)));
    }

    @Test
    public void getUser_id() {
        String country = mLocation.getUser_id();
        assertThat(country, is(equalTo("10000000")));
    }
}
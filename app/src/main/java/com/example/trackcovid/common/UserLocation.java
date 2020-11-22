package com.example.trackcovid.common;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserLocation {
    @Embedded public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public Location location;
}

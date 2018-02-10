package com.plusmobileapps.safetyapp.data.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.*;


/**
 * Created by aaronmusengo on 1/23/18.
 */

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    List<Location> getAllLocations();

    @Query("SELECT * FROM location WHERE locationId LIKE :locationId")
    Location getByLocationId(int locationId);

}


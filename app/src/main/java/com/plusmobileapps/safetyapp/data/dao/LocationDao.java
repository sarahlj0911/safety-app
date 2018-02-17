package com.plusmobileapps.safetyapp.data.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
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

    @Insert
    void insert(Location location);

    @Insert
    void insertAll(Location... locations);

    @Delete
    void delete(Location location);
}


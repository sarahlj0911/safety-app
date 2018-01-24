package com.plusmobileapps.safetyapp.data;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.*;


/**
 * Created by aaronmusengo on 1/23/18.
 */

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    List<Location> getAllLocations();

    @Query("SELECT * FROM location WHERE locationId LIKE :id")
    Location getByLocationId(int locationIf);

}


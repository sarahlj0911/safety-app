package com.plusmobileapps.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.List;

/**
 * Created by aaronmusengo on 1/23/18.
 * Updated by Robert Beerman on 3/8/2018
 */

@Dao
public interface WalkthroughDao {
    @Query("SELECT * FROM walkthroughs WHERE isDeleted == 0")
    List<Walkthrough> getAll();

    @Query("SELECT * FROM walkthroughs where walkthroughId LIKE :walkthroughId")
    Walkthrough getByWalkthroughId(String walkthroughId);

    @Query("SELECT * FROM walkthroughs WHERE isDeleted = 1")
    List<Walkthrough> getAllDeleted();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Walkthrough walkthrough);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Walkthrough... walkthroughs);

    @Delete
    void delete(Walkthrough walkthrough);

    // Used primarily for logical deletion
    @Update
    void update(Walkthrough walkthrough);
}

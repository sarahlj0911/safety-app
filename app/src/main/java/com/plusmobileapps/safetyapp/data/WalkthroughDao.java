package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Dao
public interface WalkthroughDao {
    @Query("SELECT * FROM walkthrough")
    List<Walkthrough> getAll();

    @Query("SELECT * FROM walkthrough where walkthroughId LIKE :walkthroughId")
    Walkthrough getByWalkthroughId(String walkthroughId);

    @Insert
    void insertAll(Walkthrough... walkthroughs);

    @Insert
    void insert(Walkthrough walkthrough);

    @Delete
    void delete(Walkthrough walkthrough);
}

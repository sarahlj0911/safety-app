package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    User getUser();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}

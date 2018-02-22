package com.plusmobileapps.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.plusmobileapps.safetyapp.data.entity.User;

import java.util.List;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Insert
    void insert(User user);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

package com.plusmobileapps.safetyapp.data.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.*;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Dao
public interface ResponseDao {
    @Query("SELECT * FROM responses")
    List<Response> getAll();

    @Query("SELECT * FROM responses where responseId LIKE :responseId")
    Response getByResponseId(String responseId);

    @Query("SELECT * FROM responses")
    List<Response> getAllActionItems();

    @Insert
    void insert(Response response);

    @Insert
    void insertAll(Response... responses);

    @Delete
    void delete(Response response);
}

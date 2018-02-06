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
    @Query("SELECT * FROM response")
    List<Response> getAll();

    @Query("SELECT * FROM response where responseId LIKE :responseId")
    Response getByResponseId(String responseId);

    @Query("SELECT * FROM response where walkthroughId like :walkthroughId")
    List<Response> getAllForWalkthrough(int walkthroughId);

    @Query("SELECT * FROM response where isActionItem like :actionItem")
    List<Response> getAllActionItems(int actionItem);

    @Insert
    void insertAll(Response... responses);

    @Delete
    void delete(Response response);
}

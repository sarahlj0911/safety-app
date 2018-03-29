package com.plusmobileapps.safetyapp.data.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
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

    @Query("SELECT * FROM responses WHERE walkthroughId = :walkthroughId")
    List<Response> getAllByWalkthroughId(int walkthroughId);

    @Query("SELECT * FROM responses where responseId LIKE :responseId")
    Response getByResponseId(String responseId);

    @Query("SELECT count(*) FROM responses WHERE walkthroughId = :walkthroughId")
    int getResponseCount(int walkthroughId);

    @Query("SELECT * FROM responses where isActionItem LIKE :actionItem")
    List<Response> getAllActionItems(int actionItem);

    @Query("SELECT * FROM RESPONSES where locationId LIKE :locationId AND walkthroughId LIKE :walkthroughId")
    List<Response> getResponsesForLocation(int locationId, int walkthroughId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Response response);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Response> responses);

    @Delete
    void delete(Response response);
}

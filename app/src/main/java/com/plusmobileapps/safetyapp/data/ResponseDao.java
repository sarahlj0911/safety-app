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
public interface ResponseDao {
    @Query("SELECT * FROM response")
    List<Response> getAll();

    @Query("SELECT * FROM response where responseId LIKE :responseId")
    Response getByResponseId(String responseId);

    @Query("SELECT * FROM response where walkthroughId like :walkthroughId")
    List<Response> getAllForWalkthrough(int walkthroughId);

    @Query("SELECT * FROM response where walkthroughId like :walkthroughId AND isActionItem like 'true'")
    List<Response> getAllActionItems(int walkthroughId);

    @Insert
    void insertAll(Response... responses);

    @Delete
    void delete(Response response);
}

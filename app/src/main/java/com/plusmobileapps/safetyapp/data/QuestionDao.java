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
public interface QuestionDao {
    @Query("SELECT * FROM question")
    List<Question> getAll();

    @Query("SELECT * FROM question WHERE questionId like :questionId")
    Question getByQuestionID(int questionId);

    @Query("SELECT * FROM question WHERE locationId like :locationId")
    List<Question> getAllForLocation(int locationId);
}


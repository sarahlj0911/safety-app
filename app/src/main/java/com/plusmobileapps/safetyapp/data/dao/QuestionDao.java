package com.plusmobileapps.safetyapp.data.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.plusmobileapps.safetyapp.data.entity.Question;

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

}


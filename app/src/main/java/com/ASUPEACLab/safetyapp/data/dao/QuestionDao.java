package com.ASUPEACLab.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ASUPEACLab.safetyapp.data.entity.Question;

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

    @Query("SELECT * " +
            "FROM question INNER JOIN question_mapping on question.questionId = question_mapping.questionId WHERE locationId like :locationId")
    List<Question> getQuestionsForLocationType(int locationId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Question... questions);

    @Delete
    void delete(Question question);

}


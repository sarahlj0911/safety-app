package com.plusmobileapps.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;

import java.util.List;

/**
 * Created by aaronmusengo on 2/16/18.
 */

@Dao
public interface QuestionMappingDao {
    @Query("SELECT * FROM question_mapping")
    List<QuestionMapping> getAllQuestionMappings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionMapping questionMapping);
}


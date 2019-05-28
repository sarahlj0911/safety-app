package com.ASUPEACLab.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ASUPEACLab.safetyapp.data.entity.QuestionMapping;

import java.util.List;

/**
 * Created by aaronmusengo on 2/16/18.
 */

@Dao
public interface QuestionMappingDao {
    @Query("SELECT * FROM question_mapping")
    List<QuestionMapping> getAllQuestionMappings();

    @Query("SELECT count(*) FROM question_mapping")
    int getQuestionCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionMapping questionMapping);
}


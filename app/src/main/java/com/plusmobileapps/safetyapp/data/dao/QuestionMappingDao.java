package com.plusmobileapps.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;

/**
 * Created by aaronmusengo on 2/16/18.
 */

@Dao
public interface QuestionMappingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionMapping questionMapping);
}


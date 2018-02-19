package com.plusmobileapps.safetyapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;

/**
 * Created by aaronmusengo on 2/16/18.
 */

@Dao
public interface QuestionMappingDao {
    @Insert
    void insert(QuestionMapping questionMapping);
}


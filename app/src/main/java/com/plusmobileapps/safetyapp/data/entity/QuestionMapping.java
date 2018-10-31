package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by aaronmusengo on 2/10/18.
 */

@Entity(tableName = "question_mapping")
public class QuestionMapping {

    @PrimaryKey()
    int mappingId;

    @ColumnInfo(name = "locationId")
    private int locationId;

    @ColumnInfo(name = "questionId")
    private int questionId;

    public QuestionMapping(int mappingId, int locationId, int questionId) {
        this.mappingId = mappingId;
        this.locationId = locationId;
        this.questionId = questionId;
    }

    // getters
    public int getMappingId() {
        return this.mappingId;
    }

    public int getLocationId() {
        return this.locationId;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    // setters
    public void setMappingId(int mappingId) {
        this.mappingId = mappingId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}


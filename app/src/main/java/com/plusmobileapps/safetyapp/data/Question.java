package com.plusmobileapps.safetyapp.data;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;


/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "question")
public class Question {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "questionText")
    private String questionText;

    @ColumnInfo(name = "ratingOptions")
    private String ratingOptions;

    @ColumnInfo(name = "locationID")
    private int locationID;

    public Question(int id, String questionText, String ratingOptions, int locationID) {
        this.id = id;
        this.questionText = questionText;
        this.ratingOptions = ratingOptions;
        this.locationID = locationID;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getRatingOptions() {
        return ratingOptions;
    }

    public int getLocationID() {
        return locationID;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setRatingOptions(String ratingOptions) {
        this.ratingOptions = ratingOptions;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}


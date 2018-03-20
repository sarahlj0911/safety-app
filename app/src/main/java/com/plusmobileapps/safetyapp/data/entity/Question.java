package com.plusmobileapps.safetyapp.data.entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;


/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "question")
public class Question {

    @PrimaryKey(autoGenerate = false)
    private int questionId;

    @ColumnInfo(name = "questionText")
    private String questionText;

    @ColumnInfo(name = "ratingOption1")
    private String ratingOption1;

    @ColumnInfo(name = "ratingOption2")
    private String ratingOption2;

    @ColumnInfo(name = "ratingOption3")
    private String ratingOption3;

    @ColumnInfo(name = "ratingOption4")
    private String ratingOption4;

    @ColumnInfo(name = "shortDesc")
    private String shortDescription;

    public Question(int questionId, String questionText, String shortDescription, String ratingOption1, String ratingOption2, String ratingOption3, String ratingOption4) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.shortDescription = shortDescription;
        this.ratingOption1 = ratingOption1;
        this.ratingOption2 = ratingOption2;
        this.ratingOption3 = ratingOption3;
        this.ratingOption4 = ratingOption4;
    }

    //Getters
    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getShortDescription() { return shortDescription; }

    public String getRatingOption1() { return ratingOption1; }

    public String getRatingOption2() { return ratingOption2; }

    public String getRatingOption3() { return ratingOption3; }

    public String getRatingOption4() { return ratingOption4; }


    //Setters
    public void setQuestionId(int id) {
        this.questionId = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setRatingOption1(String ratingOption1) {
        this.ratingOption1 = ratingOption1;
    }

    public void setRatingOption2(String ratingOption2) {
        this.ratingOption2 = ratingOption2;
    }

    public void setRatingOption3(String ratingOption3) {
        this.ratingOption3 = ratingOption3;
    }

    public void setRatingOption4(String ratingOption4) {
        this.ratingOption4 = ratingOption4;
    }
}


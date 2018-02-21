package com.plusmobileapps.safetyapp.data.entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.media.Image;

import java.util.zip.CheckedOutputStream;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "responses",
        foreignKeys =  {
            @ForeignKey(entity = Walkthrough.class,
                        parentColumns = "walkthroughId",
                        childColumns = "walkthroughId"),
            @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId"),
            @ForeignKey(entity = Location.class,
                        parentColumns = "locationId",
                        childColumns = "locationId"),
            @ForeignKey(entity = Question.class,
                        parentColumns = "questionId",
                        childColumns = "questionId"),
        })
public class Response {
    @PrimaryKey(autoGenerate = false)
    private int responseId;

    @ColumnInfo(name = "isActionItem")
    private int isActionItem;

    @ColumnInfo(name = "image")
    private String imagePath;

    @ColumnInfo(name = "locationId")
    private int locationId;

    @ColumnInfo(name = "timestamp")
    private String timeStamp; //TODO Convert to time stamp object

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "actionPlan")
    private String actionPlan;

    @ColumnInfo(name = "questionId")
    private int questionId;

    @ColumnInfo(name = "userId")
    private int userId;

    @ColumnInfo(name = "walkthroughId")
    private int walkthroughId;

    public Response(int responseId, int isActionItem, int locationId, String timeStamp, int rating, int priority, String actionPlan, int questionId, String imagePath, int userId, int walkthroughId) {
        this.responseId = responseId;
        this.isActionItem = isActionItem;
        this.locationId = locationId;
        this.timeStamp = timeStamp;
        this.rating = rating;
        this.priority = priority;
        this.actionPlan = actionPlan;
        this.questionId = questionId;
        this.imagePath = imagePath;
        this.userId = userId;
        this.walkthroughId = walkthroughId;
    }

    //Getters
    public int getResponseId() {
        return this.responseId;
    }

    public int getIsActionItem() {
        return this.isActionItem;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public int getLocationId() {
        return this.locationId;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public int getRating() {
        return this.rating;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getActionPlan() {
        return this.actionPlan;
    }

    public int getQuestionId() {
        return this.questionId;
    }


    public int getUserId() { return this.userId; }

    public int getWalkthroughId() { return this.walkthroughId; }

    //Setters
    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public void setIsActionItem(int isActionItem) {
        this.isActionItem = isActionItem;
    }

    public void setImage(String image) {
        if(image == null) {
            this.imagePath = "";
        } else {
            this.imagePath = imagePath;
        }
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setActionPlan(String actionPlan) {
        this.actionPlan = actionPlan;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setWalkthroughId(int walkthroughId) { this.walkthroughId = walkthroughId; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Response) {
            Response response = (Response) obj;
            if (response.getActionPlan().equals(actionPlan) &&
                    response.getIsActionItem() == isActionItem &&
                    response.getLocationId() == locationId &&
                    response.getTimeStamp().equals(timeStamp) &&
                    response.getPriority() == priority &&
                    response.getQuestionId() == questionId &&
                    response.getRating() == rating &&
                    response.getUserId() == userId &&
                    response.getWalkthroughId() == walkthroughId
                    ) {
                return true;
            }
        }
        return false;
    }

}



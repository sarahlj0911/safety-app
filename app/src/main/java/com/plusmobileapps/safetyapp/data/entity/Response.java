package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * Class intended to store responses to questions
 * asked in the walkthrough.
 *
 * Created by aaronmusengo on 1/23/18.
 * Updated by Robert Beerman on 3/18/2018
 * Updated by Travis Hawley on 4/19/19
 */

@Entity(tableName = "responses",
        primaryKeys = {"responseId", "walkthroughId", "locationId", "questionId"},
        foreignKeys = {
                @ForeignKey(entity = Walkthrough.class,
                        parentColumns = "walkthroughId",
                        childColumns = "walkthroughId",
                        onDelete = ForeignKey.CASCADE),
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

    public final static int IS_ACTION_ITEM = 1;
    public final static int NOT_ACTION_ITEM = 0;


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

    @Ignore
    private String locationName;

    @Ignore
    private String ratingText;

    @Ignore
    private String title;

    @Ignore
    private boolean persisted;

    public Response() {

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


    public int getUserId() {
        return this.userId;
    }

    public int getWalkthroughId() {
        return this.walkthroughId;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public String getTitle() {
        return this.title;
    }

    public String getRatingText() {
        return this.ratingText;
    }

    public boolean isPersisted() {
        return this.persisted;
    }

    //Setters
    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public void setIsActionItem(int isActionItem) {
        this.isActionItem = isActionItem;
    }

    /*public void setImage(String image) {
        if(image == null) {
            this.imagePath = "";
        } else {
            this.imagePath = imagePath;
        }
    }*/

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

    public void setWalkthroughId(int walkthroughId) {
        this.walkthroughId = walkthroughId;
    }

    public boolean isActionItem() {
        return isActionItem != 0;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public void setIsPersisted(boolean persisted) {
        this.persisted = persisted;
    }

    /*
    * Intended to allow direct comparison of
    * response objects.
    */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Response) {
            Response response = (Response) obj;
            return response.getActionPlan().equals(actionPlan) &&
                    response.getLocationId() == locationId &&
                    response.getQuestionId() == questionId &&
                    response.getWalkthroughId() == walkthroughId;
        }
        return false;
    }

    /*
     * Intended to allow for response objects
     * to be output as strings for easier
     * examination of the contents and
     * transfer to other formats.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("responseId=").append(responseId);
        sb.append(", isActionItem=").append(isActionItem);
        sb.append(", imagePath='").append(imagePath).append('\'');
        sb.append(", locationId=").append(locationId);
        sb.append(", timeStamp='").append(timeStamp).append('\'');
        sb.append(", rating=").append(rating);
        sb.append(", priority=").append(priority);
        sb.append(", actionPlan='").append(actionPlan).append('\'');
        sb.append(", questionId=").append(questionId);
        sb.append(", userId=").append(userId);
        sb.append(", walkthroughId=").append(walkthroughId);
        sb.append(", locationName='").append(locationName).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", persisted='").append(persisted).append('\'');
        sb.append('}');
        return sb.toString();
    }
}



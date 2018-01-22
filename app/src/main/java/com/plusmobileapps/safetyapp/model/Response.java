package com.plusmobileapps.safetyapp.model;

import com.plusmobileapps.safetyapp.model.Priority;

import java.util.Date;

/**
 * Created by Robert Beerman on 1/21/2018.
 */

public class Response {
    private int id;
    private int walkthroughID;
    private int locationID;
    private int questionID;
    private Date lastUpdated;
    private int rating;
    private Priority priority;
    private String actionPlan;
    private boolean isActionItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalkthroughID() {
        return walkthroughID;
    }

    public void setWalkthroughID(int walkthroughID) {
        this.walkthroughID = walkthroughID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getActionPlan() {
        return actionPlan;
    }

    public void setActionPlan(String actionPlan) {
        this.actionPlan = actionPlan;
    }

    public boolean isActionItem() {
        return isActionItem;
    }

    public void setActionItem(boolean actionItem) {
        isActionItem = actionItem;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", walkthroughID=" + walkthroughID +
                ", locationID=" + locationID +
                ", questionID=" + questionID +
                ", lastUpdated=" + lastUpdated +
                ", rating=" + rating +
                ", priority=" + priority +
                ", actionPlan='" + actionPlan + '\'' +
                ", isActionItem=" + isActionItem +
                '}';
    }
}


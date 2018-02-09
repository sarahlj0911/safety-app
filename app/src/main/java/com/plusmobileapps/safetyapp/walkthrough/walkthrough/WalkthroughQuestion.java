package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.graphics.Bitmap;

import com.plusmobileapps.safetyapp.model.Priority;

import java.util.ArrayList;

/**
 * Created by Andrew on 11/10/2017.
 */

public class WalkthroughQuestion {
    private String location;
    private String description;
    private ArrayList<String> options;
    private String rating;
    private Priority priority;
    private boolean isActionItem;
    private String actionPlan;
    private Bitmap photo;

    public WalkthroughQuestion(String location, String description, ArrayList<String> options, Priority priority) {
        this.location = location;
        this.description = description;
        this.options = options;
        this.priority = priority;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getActionPlan() {
        return actionPlan;
    }

    public void setActionPlan(String actionPlan) {
        this.actionPlan = actionPlan;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public boolean isActionItem() {
        return isActionItem;
    }

    public void setActionItem(boolean actionItem) {
        isActionItem = actionItem;
    }
}

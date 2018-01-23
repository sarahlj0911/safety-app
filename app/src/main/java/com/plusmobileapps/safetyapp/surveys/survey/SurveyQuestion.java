package com.plusmobileapps.safetyapp.surveys.survey;

import android.graphics.Bitmap;

import com.plusmobileapps.safetyapp.model.Priority;

import java.util.ArrayList;

/**
 * Created by Andrew on 11/10/2017.
 */

public class SurveyQuestion {
    private String location;
    private String description;
    private ArrayList<String> options;
    private Priority priority;
    private String actionPlan;
    private Bitmap photo;

    public SurveyQuestion(String location, String description, ArrayList<String> options, Priority priority) {
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
}

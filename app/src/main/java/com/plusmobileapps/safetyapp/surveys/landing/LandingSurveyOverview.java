package com.plusmobileapps.safetyapp.surveys.landing;

/**
 * Created by Andrew on 11/13/2017.
 */

public class LandingSurveyOverview {
    private String title;
    private String date;
    private String time;
    private boolean inProgress = false;
    private int progress = 0;

    public LandingSurveyOverview(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

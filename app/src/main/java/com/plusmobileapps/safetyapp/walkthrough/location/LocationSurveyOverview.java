package com.plusmobileapps.safetyapp.walkthrough.location;

/**
 * Crea
 */

public class LocationSurveyOverview {
    private boolean finished = false;
    private String title = "";
    private Integer progress = 0;

    public LocationSurveyOverview(String title){
        this.title = title;
    }

    public LocationSurveyOverview(String title, int progress){
        this.title = title;
        this.progress = progress;
    }

    public LocationSurveyOverview(String title, boolean finished){
        this.title = title;
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}

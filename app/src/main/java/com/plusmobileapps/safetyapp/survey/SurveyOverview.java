package com.plusmobileapps.safetyapp.survey;

/**
 * Crea
 */

public class SurveyOverview {
    private boolean finished = false;
    private String title = "";
    private Integer progress = 0;

    public SurveyOverview(String title){
        this.title = title;
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

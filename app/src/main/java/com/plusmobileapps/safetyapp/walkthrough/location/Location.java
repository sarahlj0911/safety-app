package com.plusmobileapps.safetyapp.walkthrough.location;

public class Location {
    private boolean finished = false;
    private String title = "";
    private Integer progress = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id = 0;

    public Location(String title){
        this.title = title;
    }

    public Location(String title, int progress){
        this.title = title;
        this.progress = progress;
    }

    public Location(String title, boolean finished){
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

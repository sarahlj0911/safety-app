package com.plusmobileapps.safetyapp.walkthrough.landing;

/**
 * Created by Andrew on 11/13/2017.
 */

public class WalkthroughOverview {
    private String title;
    private String date;
    private String time;
    private int progress = 0;
    private long walkthroughId = 0L;

    public WalkthroughOverview(String title){
        this.title = title;
    }

    public WalkthroughOverview(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
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

    public long getWalkthroughId() {
        return walkthroughId;
    }

    public void setWalkthroughId(long walkthroughId) {
        this.walkthroughId = walkthroughId;
    }

    public boolean isInProgress() {
        if(progress == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getModified() {
        if (this.isInProgress()) {
            return "Last Modified:";
        } else {
            return "Completed On:";
        }
    }
}

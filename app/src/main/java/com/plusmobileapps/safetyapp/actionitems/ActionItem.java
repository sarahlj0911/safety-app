package com.plusmobileapps.safetyapp.actionitems;

import android.graphics.Bitmap;

/**
 * Created by Andrew on 11/8/2017.
 */

public class ActionItem {
    private int status;
    private String title;
    private String location;
    private Bitmap photo;
    private String description;

    public enum Status {
        GREEN, YELLOW, RED
    }

    public ActionItem(String title, String location, int status, String description){
        this.title = title;
        this.location = location;
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}

package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

@Entity(tableName = "actionItem")
public class ActionItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "location")
    private String location;


    @ColumnInfo(name = "description")
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

    //Getters
    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    //Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

}

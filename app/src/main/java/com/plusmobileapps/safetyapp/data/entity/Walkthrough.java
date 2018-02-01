package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "walkthrough")
public class Walkthrough {
    @PrimaryKey(autoGenerate = false)
    private int walkthroughId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "percentComplete")
    private int percentComplete;

    @ColumnInfo(name = "createdDate")
    private String createdDate;

    @ColumnInfo(name = "lastUpdatedDate")
    private String lastUpdatedDate;

    public Walkthrough(int walkthroughId, String name, int percentComplete, String createdDate, String lastUpdatedDate) {
        this.walkthroughId = walkthroughId;
        this.name = name;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    //Getters
    public int getWalkthroughId() { return this.walkthroughId; }

    public String getName() { return this.name; }

    public int getPercentComplete() { return this.percentComplete; }

    public String getCreatedDate() { return this.createdDate; }

    public String  getLastUpdatedDate() { return this.lastUpdatedDate; }

    //Setters
    public void setWalkthroughId(int walkthroughId) {
        this.walkthroughId = walkthroughId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public void setCreatedDate(String  createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}


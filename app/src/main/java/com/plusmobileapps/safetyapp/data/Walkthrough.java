package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "walkthrough")
public class Walkthrough {
    @PrimaryKey(autoGenerate = true)
    private int walkthroughId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "percentComplete")
    private int percentComplete;

    @ColumnInfo(name = "createdDate")
    private Date createdDate;

    @ColumnInfo(name = "lastUpdatedDate")
    private Date lastUpdatedDate;

    public Walkthrough(int walkthroughId, String name, int percentComplete, Date createdDate, Date lastUpdatedDate) {
        this.walkthroughId = walkthroughId;
        this.name = name;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    //Getters
    public int getWalkthroughId() {

        return this.walkthroughId;
    }

    public String getName() {

        return this.name;
    }

    public int getPercentComplete() {

        return this.percentComplete;
    }

    public Date getCreatedDate() {

        return this.createdDate;
    }

    public Date getLastUpdatedDate() {

        return this.lastUpdatedDate;
    }

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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}


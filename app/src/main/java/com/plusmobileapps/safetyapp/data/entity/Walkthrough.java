package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "walkthroughs",
        foreignKeys = @ForeignKey(entity = School.class,
                                  parentColumns = "schoolId",
                                  childColumns = "schoolId"))
public class Walkthrough {
    @PrimaryKey(autoGenerate = false)
    private int walkthroughId;

    @ColumnInfo(name = "schoolId")
    private int schoolId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "percentComplete")
    private double percentComplete;

    @ColumnInfo(name = "createdDate")
    private String createdDate;

    @ColumnInfo(name = "lastUpdatedDate")
    private String lastUpdatedDate;

    public Walkthrough(int walkthroughId, String name, double percentComplete, String createdDate, String lastUpdatedDate, int schoolId) {
        this.walkthroughId = walkthroughId;
        this.name = name;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.schoolId = schoolId;
    }

    //Getters
    public int getWalkthroughId() { return this.walkthroughId; }

    public String getName() { return this.name; }

    public double getPercentComplete() { return this.percentComplete; }

    public String getCreatedDate() { return this.createdDate; }

    public String  getLastUpdatedDate() { return this.lastUpdatedDate; }

    public int getSchoolId() { return this.schoolId; }

    //Setters
    public void setWalkthroughId(int walkthroughId) {
        this.walkthroughId = walkthroughId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public void setCreatedDate(String  createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}


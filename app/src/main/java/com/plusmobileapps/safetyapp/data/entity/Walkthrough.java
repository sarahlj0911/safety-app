package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "walkthroughs",
        foreignKeys = @ForeignKey(entity = School.class,
                                  parentColumns = "schoolId",
                                  childColumns = "schoolId"))
public class Walkthrough {

    @PrimaryKey(autoGenerate = true)
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

    public Walkthrough(String name) {
        this.name = name;
        percentComplete = 0.0;
        Date date = new Date();
        createdDate = date.toString();
        lastUpdatedDate = createdDate;
        schoolId = 1;

    }
    

    //Getters
    public int getWalkthroughId() { return this.walkthroughId; }

    public String getName() { return this.name; }

    public double getPercentComplete() { return this.percentComplete; }

    public String getCreatedDate() { return this.createdDate; }

    public String  getLastUpdatedDate() { return this.lastUpdatedDate; }

    public int getSchoolId() { return this.schoolId; }

    public String getDate(String date) {
        String[] tmp = date.split("-");
        String temp = tmp[1] + " " + tmp[2] + ", " + tmp[tmp.length - 1];
        return temp;
    }

    public String getTime(String date) {
        String[] tmp = date.split(" ");
        int hour =  Integer.parseInt(tmp[3].substring(0, 2));
        if(hour < 12) {
            return tmp[3].substring(0, 5) + " AM";
        } else if(hour == 12) {
            return tmp[3].substring(0, 5) + " PM";
        } else {
            return hour - 12 + tmp[3].substring(2, 5) + " PM";
        }
    }

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


    public boolean isInProgress() {
        int complete = ((int) percentComplete);
        return complete != 100;
    }


}


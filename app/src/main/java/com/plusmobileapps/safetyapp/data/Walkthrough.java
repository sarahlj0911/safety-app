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
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "percentComplete")
    private int percentComplete;

    @ColumnInfo(name = "createdDate")
    private Date createdDate;

    @ColumnInfo(name = "lastUpdatedDate")
    private Date lastUpdatedDate;

    public Walkthrough(int id, String name, int percentComplete, Date createdDate, Date lastUpdatedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    //Getters
    public int getId() {
        return this.id;
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
}


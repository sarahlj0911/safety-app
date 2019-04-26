package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Class intended for the creation and manipulation of
 * school objects in the code to allow for different
 * schools to have sole access to their own
 * information.
 *
 * Created by aaronmusengo on 2/7/18.
 * Updated by Travis Hawley on 4/19/19
 */

@Entity(tableName = "schools")
public class School {
    @PrimaryKey
    private int schoolId;

    @ColumnInfo(name = "schoolName")
    private String schoolName;

    @ColumnInfo(name = "remoteId")
    private int remoteId;

    // TODO Add isRegistered field

    public School(int schoolId, String schoolName) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.remoteId = 0;
    }

    //getters
    public int getSchoolId() {
        return this.schoolId;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public int getRemoteId() {
        return this.remoteId;
    }

    //setters
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public boolean isRegistered() {
        return remoteId > 0;
    }
}

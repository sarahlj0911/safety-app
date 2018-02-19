package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "user",
        foreignKeys = @ForeignKey(entity = School.class,
                    parentColumns = "schoolId",
                    childColumns = "schoolId"))
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;

    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "schoolId")
    private int schoolId;

    @ColumnInfo(name = "emailAddress")
    private String emailAddress;

    public User(int userId, int schoolId, String emailAddress, String userName) {
        this.userId = userId;
        this.schoolId = schoolId;
        this.emailAddress = emailAddress;
    }

    //Getters
    public int getUserId() {
        return this.userId;
    }

    public int getSchoolId() {
        return this.schoolId;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    //Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

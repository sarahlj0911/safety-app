package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * Class intended for the creation and manipulation of
 * location objects in the code.
 *
 * Created by aaronmusengo on 1/23/18.
 * Updated by Travis Hawley on 4/19/19
 */

@Entity(tableName = "location")
public class Location {
    @PrimaryKey()
    int locationId;

    @ColumnInfo(name = "type")
    private String type; //TODO Create Type Enum

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "locationInstruction")
    private String locationInstruction;

    @Ignore
    private boolean isFinished;

    @Ignore
    private int progess;

    public Location(int locationId, String type, String name, String locationInstruction) {
        this.locationId = locationId;
        this.type = type;
        this.name = name;
        this.locationInstruction = locationInstruction;
    }

    //Getters
    public int getLocationId() {
        return this.locationId;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getLocationInstruction() {
        return this.locationInstruction;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public int getProgress() {
        return this.progess;
    }

    //Setters
    public void setId(int id) {
        this.locationId = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationInstruction(String locationInstruction) {
        this.locationInstruction = locationInstruction;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setProgess(int progess) {
        this.progess = progess;
    }
}

package com.plusmobileapps.safetyapp.data.entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * Created by aaronmusengo on 1/23/18.
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
}

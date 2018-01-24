package com.plusmobileapps.safetyapp.data;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
/**
 * Created by aaronmusengo on 1/23/18.
 */

@Entity(tableName = "location")
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "type")
    private String type; //TODO Create Type Enum

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "walkthroughID")
    private int walkthroughID;

    public Location(int id, String type, String name, int walkthroughID) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.walkthroughID = walkthroughID;
    }

    //Getters
    public int getid() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getWalkthroughID() {
        return this.walkthroughID;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWalkthroughID(int walkthroughID) {
        this.walkthroughID = walkthroughID;
    }
}

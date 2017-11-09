package com.plusmobileapps.safetyapp.survey;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Survey {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "finished")
    private boolean finished = false;

    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "progress")
    private Integer progress = 0;
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}

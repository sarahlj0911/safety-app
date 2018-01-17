package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ActionItemDao {
    @Query("SELECT * FROM actionItem")
    List<ActionItem> getAll();

    @Query("SELECT * FROM actionItem where id LIKE :id")
    ActionItem findById(String id);

    @Query("SELECT COUNT(*) FROM actionItem")
    int countActionItems();

    @Insert
    void insertAll(ActionItem... actionItems);

    @Delete
    void delete(ActionItem actionItem);
}

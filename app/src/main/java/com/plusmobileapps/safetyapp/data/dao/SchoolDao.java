package com.plusmobileapps.safetyapp.data.dao;
import com.plusmobileapps.safetyapp.data.entity.School;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

/**
 * Created by aaronmusengo on 2/16/18.
 */
@Dao
public interface SchoolDao {

    //Not sure about this. Obviously it will only return 1 element, but don't like want to
    //do a SELECT * and not be prepared for more than one.
    @Query("SELECT * FROM schools")
    List<School> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(School school);
}

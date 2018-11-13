ackage com.plusmobileapps.safetyapp.data.dao;

import com.plusmobileapps.safetyapp.data.entity.School;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDaoo {

    //Not sure about this. Obviously it will only return 1 element, but don't like want to
    //do a SELECT * and not be prepared for more than one.
    @Query("SELECT * FROM user")
    List<School> getAll();

    @Query("SELECT schoolId, schoolName, remoteId FROM schools WHERE schoolId = 1")
    School get();

    @Delete(onConflict = OnConflictStrategy.REPLACE)
    void delete(User user);
}

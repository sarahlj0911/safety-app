package com.plusmobileapps.safetyapp.data.singletons;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.entity.School;

import java.security.cert.Extension;
import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class Schools {
    private AppDatabase db;
    private SchoolDao schoolDao;
    private List<School> schoolList;
    private static final Schools ourInstance = new Schools();

    public static Schools getInstance() {
        return ourInstance;
    }

    private Schools() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        schoolDao = db.schoolDao();
        populateSchools();
    }

    private void populateSchools() {
        schoolList = schoolDao.getAll();
    }

    //Public Methods
    public List<School> getAllSchools() {
        return this.schoolList;
    }

    public School getSchoolWithId(int schoolId) {
        for (int i = 0; i < schoolList.size(); i++) {
            School tempSchool = schoolList.get(i);
            if (tempSchool.getSchoolId() == schoolId) {
                return tempSchool;
            }
        }
        return null;
    }

    public void addSchool(School school) {
        schoolList.add(school);

        try {
            schoolDao.insert(school);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

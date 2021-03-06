package com.plusmobileapps.safetyapp.data.loadTasks;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.List;

public class InsertLocationsTask extends AsyncTask<Void, Void, Void> {

    private List<Location> locations;

    public InsertLocationsTask(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        LocationDao locationDao = AppDatabase.getAppDatabase(MyApplication.getAppContext()).locationDao();
        for (int i = 0; i < locations.size(); i++) {
            locationDao.insert(locations.get(i));
        }
        return null;
    }
}

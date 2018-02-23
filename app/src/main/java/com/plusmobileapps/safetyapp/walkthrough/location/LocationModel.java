package com.plusmobileapps.safetyapp.walkthrough.location;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronmusengo on 2/22/18.
 */

public class LocationModel extends AsyncTask<Void, Void, List<Location>> {
    private List<Location> locations = new ArrayList<>();
    private AppDatabase db;

    public LocationModel() {
        //Empty Constructor
    }

    @Override
    protected List<Location> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        LocationDao locationDao = db.locationDao();
        locations = locationDao.getAllLocations();
        return locations;
    }

    @Override
    protected void onPostExecute(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return this.locations;
    }

}

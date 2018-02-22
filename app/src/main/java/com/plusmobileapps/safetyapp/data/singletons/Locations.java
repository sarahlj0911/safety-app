package com.plusmobileapps.safetyapp.data.singletons;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class Locations {
    private static final Locations ourInstance = new Locations();

    private AppDatabase db;
    private LocationDao locationDao;
    private List<Location> locationList;

    public static Locations getInstance() {
        return ourInstance;
    }

    private Locations() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        locationDao = db.locationDao();
        populateLocations();
    }

    
    private void populateLocations() {
        new AsyncTask<Void, Void, List<Location>>() {

            @Override
            protected List doInBackground(Void... params) {
                return locationDao.getAllLocations();
            }

            @Override
            protected void onPostExecute(List locations) {
                locationList = locations;
            }
        }.execute();
    }

    //public methods
    public List<Location> getAllLocations() {
        return this.locationList;
    }

    public Location getLocation(int locationId) {
        for (int i = 0; i < locationList.size(); i++) {
            Location templocation = locationList.get(i);
            if (templocation.getLocationId() == locationId) {
                return templocation;
            }
        }
        return null;
    }

    public void addLocation(Location location) {
        locationList.add(location);

        try {
            locationDao.insert(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

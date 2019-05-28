package com.ASUPEACLab.safetyapp.walkthrough.location;

import android.os.AsyncTask;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.LocationDao;
import com.ASUPEACLab.safetyapp.data.entity.Location;

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

}

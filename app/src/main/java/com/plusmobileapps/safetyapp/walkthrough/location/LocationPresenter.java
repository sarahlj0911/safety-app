package com.plusmobileapps.safetyapp.walkthrough.location;


import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocationPresenter implements LocationContract.Presenter {

    private LocationContract.View view;
    private AsyncTask<Void, Void, List<Location>> loadLocations = new LocationModel().execute();
    private List<Location> locations;

    public LocationPresenter(LocationContract.View view) {
        this.view = view;
        try {
            this.locations = loadLocations.get();
        } catch (InterruptedException | ExecutionException e)  {
            e.printStackTrace();
        }
        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadLocations();
    }

    @Override
    public void loadLocations() {
        view.showLocations(locations);
    }

    @Override
    public void locationClicked(Location location) {
        view.openRequestedLocation(location.getLocationId());
    }

    @Override
    public void backButtonPressed() {
        view.navigateBack();
    }

    @Override
    public void createLocationClicked() {

    }
}

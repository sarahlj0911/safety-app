package com.ASUPEACLab.safetyapp.walkthrough.location;


import android.os.AsyncTask;

import com.ASUPEACLab.safetyapp.data.entity.Location;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocationPresenter implements LocationContract.Presenter {

    private LocationContract.View view;
    private AsyncTask<Void, Void, List<Location>> loadLocations = new LocationModel().execute();
    private List<Location> locations;
    private int walkthroughId;

    public LocationPresenter(LocationContract.View view, int walkthroughId) {
        this.view = view;
        this.walkthroughId = walkthroughId;
        try {
            this.locations = loadLocations.get();
        } catch (InterruptedException | ExecutionException e) {
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
        view.openRequestedLocation(location.getLocationId(), location.getName(), walkthroughId);
    }

    @Override
    public void backButtonPressed() {
        view.navigateBack();
    }

    @Override
    public void createLocationClicked() {

    }
}

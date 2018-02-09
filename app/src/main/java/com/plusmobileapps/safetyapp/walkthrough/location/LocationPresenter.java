package com.plusmobileapps.safetyapp.walkthrough.location;

import java.util.ArrayList;

public class LocationPresenter implements LocationContract.Presenter {

    private LocationContract.View view;
    private ArrayList<Location> locations = new LocationFakeModel().getLocations();

    public LocationPresenter(LocationContract.View view) {
        this.view = view;
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
        view.openRequestedLocation(location.getTitle());
    }

    @Override
    public void backButtonPressed() {
        view.navigateBack();
    }

    @Override
    public void createLocationClicked() {

    }
}

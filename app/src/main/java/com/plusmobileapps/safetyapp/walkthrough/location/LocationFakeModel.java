package com.plusmobileapps.safetyapp.walkthrough.location;


import java.util.ArrayList;

public class LocationFakeModel {

    ArrayList<Location> locations = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public LocationFakeModel() {
        createTitles();
        createNewWalkthrough();
    }

    private void createTitles(){
        titles.add("Bathroom");
        titles.add("Classroom 1");
        titles.add("Bathroom 2");
        titles.add("Locker Room");
        titles.add("Field");
        titles.add("Office");
        titles.add("Classroom 2");
        titles.add("Bathroom");
        titles.add("Classroom 1");
        titles.add("Bathroom 2");
        titles.add("Locker Room");
        titles.add("Field");
        titles.add("Office");
        titles.add("Classroom 2");
    }

    private void createNewWalkthrough() {
        for (String title : titles) {
            locations.add(new Location(title));
        }
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }
}

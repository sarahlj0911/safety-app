package com.plusmobileapps.safetyapp.walkthrough.location;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

import java.util.ArrayList;

public interface LocationContract {

    interface View extends BaseView<Presenter> {
        void showLocations(ArrayList<Location> locations);
        void openRequestedLocation(String location);
        void navigateBack();
    }

    interface Presenter extends BasePresenter {
        void loadLocations();
        void locationClicked(Location location);
        void backButtonPressed();
        void createLocationClicked();
    }
}

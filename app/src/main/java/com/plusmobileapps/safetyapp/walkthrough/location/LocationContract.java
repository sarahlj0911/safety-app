package com.plusmobileapps.safetyapp.walkthrough.location;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.List;

public interface LocationContract {

    interface View extends BaseView<Presenter> {
        void showLocations(List<Location> locations);
        void openRequestedLocation(int locationId);
        void navigateBack();
    }

    interface Presenter extends BasePresenter {
        void loadLocations();
        void locationClicked(Location location);
        void backButtonPressed();
        void createLocationClicked();
    }
}

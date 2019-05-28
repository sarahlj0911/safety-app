package com.ASUPEACLab.safetyapp.walkthrough.location;

import com.ASUPEACLab.safetyapp.BasePresenter;
import com.ASUPEACLab.safetyapp.BaseView;
import com.ASUPEACLab.safetyapp.data.entity.Location;

import java.util.List;

public interface LocationContract {

    interface View extends BaseView<Presenter> {
        void showLocations(List<Location> locations);

        void openRequestedLocation(int locationId, String locationName, int walkthroughId);

        void navigateBack();
    }

    interface Presenter extends BasePresenter {
        void loadLocations();

        void locationClicked(Location location);

        void backButtonPressed();

        void createLocationClicked();
    }
}

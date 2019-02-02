package com.plusmobileapps.safetyapp.signup;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.data.entity.School;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public interface SignupContract {

    interface View extends BaseView<Presenter> {
        void displayInvalidEmailError(boolean show);

        void displayNoEmailError(boolean show);

        void displayNoNameError(boolean show);

        void displayNoSchoolError(boolean show);

        void displayNoPasswordError(boolean show);

        void launchHomeScreen();

        void populateSchoolSpinner(ArrayList<String> schools);
    }

    interface Presenter extends BasePresenter {
        void processFormInput(Map<String, String> formInput);

        void nameTextAdded();

        void emailTextAdded();

        void schoolNameTextAdded();

        void passwordTextAdded();
    }
}

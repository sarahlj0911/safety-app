package com.plusmobileapps.safetyapp.signup;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

import java.util.ArrayList;
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

        void displayInvalidPasswordError(boolean show);

        void launchHomeScreen();

        void populateSchoolSpinner(ArrayList<String> schools);

        void displayInvalidPasswordLengthError(boolean b);
    }

    interface Presenter extends BasePresenter {
        Boolean processFormInput(Map<String, String> formInput);

        void nameTextAdded();

        void emailTextAdded();

        void passwordTextAdded();

        void schoolNameTextAdded();
    }
}

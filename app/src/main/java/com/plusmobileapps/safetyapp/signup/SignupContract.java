package com.plusmobileapps.safetyapp.signup;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public interface SignupContract {

    interface View extends BaseView<Presenter> {
        void displayErrors(List<String> errorMessages);
    }

    interface Presenter extends BasePresenter {
        boolean validateForm(Map<String, String> formInput);
        boolean saveSignupData(Map<String, String> formInput);
        boolean processFormInput(Map<String, String> formInput);
    }
}

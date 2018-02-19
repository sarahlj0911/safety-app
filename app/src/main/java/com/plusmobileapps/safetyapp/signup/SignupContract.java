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
        void validateForm(Map<String, String> formInput);
        //void setIsValid(boolean isValid);
        boolean isValid();
        void saveSignupData(Map<String, String> formInput);
        //void setIsSaved(boolean isSaved);
        boolean isSaved();
    }
}

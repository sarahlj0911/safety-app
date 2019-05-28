package com.ASUPEACLab.safetyapp.login;

import com.ASUPEACLab.safetyapp.BasePresenter;
import com.ASUPEACLab.safetyapp.BaseView;

import java.util.Map;

/**
 * Created by Jeremy Powell on 1/28/2019.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void displayInvalidEmailError(boolean show);
        void displayNoEmailError(boolean show);
        void displayNoPasswordError(boolean show);
        void launchHomeScreen();
    }

    interface Presenter extends BasePresenter {
        boolean processFormInput(Map<String, String> formInput);

        void emailTextAdded();
        void passwordTextAdded();

        void displayInvalidEmailError();

        void displayNoEmailError();

        void displayNoPasswordError();

        void launchHomeScreen();
    }

}

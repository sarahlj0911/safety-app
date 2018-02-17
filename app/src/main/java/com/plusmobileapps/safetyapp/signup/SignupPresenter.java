package com.plusmobileapps.safetyapp.signup;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public class SignupPresenter implements SignupContract.Presenter {

    private static final String TAG = "SignupPresenter";
    private SignupContract.View view;

    public SignupPresenter(SignupContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }
}

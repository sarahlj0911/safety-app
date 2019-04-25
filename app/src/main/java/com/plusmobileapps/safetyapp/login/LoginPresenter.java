package com.plusmobileapps.safetyapp.login;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Jeremy Powell on 1/28/2019.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";
    private LoginContract.View view;
    static final String EMAIL_INPUT = "email";
    static final String PASSWORD_INPUT = "password";

    LoginPresenter(LoginContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public boolean processFormInput(Map<String, String> formInput) {
        boolean isValidInput = true;

        // Code section by Robert Beerman
        String email = formInput.get(EMAIL_INPUT);

        if (email != null) {
            if (isEmpty(email)) {
                view.displayNoEmailError(true);
                isValidInput = false;
            } else if (!email.contains("@")) {
                view.displayInvalidEmailError(true);
                isValidInput = false;
            } else {
                view.displayNoEmailError(false);
            }
        }

        if (formInput.get(PASSWORD_INPUT) != null) {
            if (isEmpty(Objects.requireNonNull(formInput.get(PASSWORD_INPUT)))) {
                view.displayNoPasswordError(true);
                isValidInput = false; }
            else { view.displayNoPasswordError(false); }
        }

        return isValidInput;
    }

    @Override
    public void emailTextAdded() {
        view.displayNoEmailError(false);
    }

    @Override
    public void passwordTextAdded() {
        view.displayNoPasswordError(false);
    }

    @Override
    public void displayInvalidEmailError() { }

    @Override
    public void displayNoEmailError() { }

    @Override
    public void displayNoPasswordError() { }

    @Override
    public void launchHomeScreen() { }

    @Override
    public void start() { }

    private boolean isEmpty(String input) {
        return input.isEmpty() || input.trim().equals("") || input.length() <= 2;
    }
}

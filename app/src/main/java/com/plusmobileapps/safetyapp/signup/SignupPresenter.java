package com.plusmobileapps.safetyapp.signup;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.User;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public class SignupPresenter implements SignupContract.Presenter {

    static final String NAME_INPUT = "name";
    static final String EMAIL_INPUT = "email";
    static final String PASSWORD_INPUT = "password";
    static final String PASSWORD_INPUT_CHECK = "passwordCheck";
    static final String SCHOOL_NAME_INPUT = "school_name";
    static final String ROLE_INPUT = "role";

    private static final String TAG = "SignupPresenter";
    private SignupContract.View view;
    private boolean isSaved;
    private String errorMessage;
    private ArrayList<String> schools;
    private CognitoUserPool userPool;


    SignupPresenter(SignupContract.View view) {
        this.view = view;
        view.setPresenter(this);
        errorMessage = "";
        schools = new ArrayList<>();
    }

    @Override
    public void start() {
        isSaved = true;
    }

    @Override
    public Boolean processFormInput(Map<String, String> formInput) {
        boolean isValidInput = true;

        String name = formInput.get(NAME_INPUT);
        if (name != null) {
            if (isEmpty(name)) {
                view.displayNoNameError(true);
                isValidInput = false; }
            else {
                view.displayNoNameError(false); } }
        else {
            view.displayNoNameError(true);
            isValidInput = false;
        }

        String email = formInput.get(EMAIL_INPUT);
        if (email != null) {
            if (isEmpty(email)) {
                view.displayNoEmailError(true);
                isValidInput = false; }
            else if (!email.contains("@")) {
                view.displayInvalidEmailError(true);
                isValidInput = false; }
            else {
                view.displayNoEmailError(false);
                view.displayInvalidEmailError(false); } }
        else {
            view.displayNoEmailError(true);
            isValidInput = false;
        }

            String password = formInput.get(PASSWORD_INPUT);
        if (password != null) {
            if (isEmpty(password)) {
                view.displayNoPasswordError(true);
                isValidInput = false; }
            else {
                view.displayNoPasswordError(false); } }
        else {
            view.displayNoPasswordError(true);
            isValidInput = false;
        }

        String passwordCheck = formInput.get(PASSWORD_INPUT_CHECK);
        if (passwordCheck != null) {
            if (isEmpty(passwordCheck)) {
                view.displayNoPasswordCheckError(true);
                isValidInput = false; }
            else if (!passwordCheck.equals(password)) {
                view.displayNoPasswordCheckErrorNoMatch(true);
                isValidInput = false; }
            else {
                view.displayNoPasswordCheckError(false);
                view.displayNoPasswordCheckErrorNoMatch(false); } }
        else {
            view.displayNoPasswordCheckError(true);
            isValidInput = false;
        }


        if (isValidInput) {
            saveSignupData(formInput);
            return true; }
        else return false;
    }

    private boolean isEmpty(String input) {
        return input.isEmpty() || input.trim().equals("") || input.length() <= 2;
    }

    private void saveSignupData(Map<String, String> formInput) {
        String schoolName = formInput.get(SCHOOL_NAME_INPUT);
        School school = new School(1, schoolName);

        AsyncTask<Void, Void, Boolean> saveSchoolTask = new SaveSchoolTask(school).execute();

        try {
            saveSchoolTask.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(TAG, "Problem saving school");
            Log.d(TAG, e.getMessage());
            errorMessage = "Unable to save input";
        }

        Log.d(TAG, "School saved!");

        String userName = formInput.get(NAME_INPUT);
        String email = formInput.get(EMAIL_INPUT);
        String password = formInput.get(PASSWORD_INPUT);
        String role = formInput.get(ROLE_INPUT);

        User user = new User(1, 1, email, userName, role);
        AsyncTask<Void, Void, Boolean> saveUserTask = new SaveUserTask(user).execute();

        try {
            isSaved = saveUserTask.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(TAG, "Issue saving user");
            Log.d(TAG, e.getMessage());
            errorMessage = "Unable to save input";
        }
    }

    @Override
    public void nameTextAdded() {
        view.displayNoNameError(false);
    }

    @Override
    public void emailTextAdded() {
        view.displayNoEmailError(false);
    }

    @Override
    public void passwordTextAdded() { view.displayNoPasswordError(false); }

    @Override
    public void passwordCheckTextAdded() { view.displayNoPasswordCheckError(false); }

    @Override
    public void schoolNameTextAdded() { view.displayNoSchoolError(false); }


}

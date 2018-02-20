package com.plusmobileapps.safetyapp.signup;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public class SignupPresenter implements SignupContract.Presenter {

    private static final String TAG = "SignupPresenter";
    private SignupContract.View view;
    private boolean isSaved;
    private String errorMessage;

    SignupPresenter(SignupContract.View view) {
        this.view = view;
        view.setPresenter(this);
        errorMessage = "";
    }

    @Override
    public void start() {
        isSaved = true;
    }

    @Override
    public boolean processFormInput(Map<String, String> formInput) {
        if (!validateForm(formInput)) {
            view.displayError(errorMessage);
            return false;
        }

        if (!saveSignupData(formInput)) {
            view.displayError(errorMessage);
            return false;
        }

        return true;
    }

    @Override
    public boolean validateForm(Map<String, String> formInput) {

        for (HashMap.Entry<String, String> entry : formInput.entrySet()) {
            String value = entry.getValue();
            if (value.isEmpty() || value.trim().equals("") || value.length() <= 2) {
                errorMessage = entry.getKey() + " is required";
                return false;
            }

            // Validate email format
            if (entry.getKey().equals("Email") && !value.contains("@")){
                errorMessage = "Invalid email format";
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean saveSignupData(Map<String, String> formInput) {
        String schoolName = formInput.get("School Name");
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

        String userName = formInput.get("Name");
        String email = formInput.get("Email");
        String role = formInput.get("Role");

        User user = new User(1, 1, email, userName, role);

        AsyncTask<Void, Void, Boolean> saveUserTask = new SaveUserTask(user).execute();

        try {
            isSaved = saveUserTask.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(TAG, "Problem saving user");
            Log.d(TAG, e.getMessage());
            errorMessage = "Unable to save input";
        }

        return isSaved;
    }
}

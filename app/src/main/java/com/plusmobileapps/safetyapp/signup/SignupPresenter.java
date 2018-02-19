package com.plusmobileapps.safetyapp.signup;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.UserDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public class SignupPresenter implements SignupContract.Presenter {

    private static final String TAG = "SignupPresenter";
    private SignupContract.View view;
    private boolean isValid;
    private boolean isSaved;
    private List<String> errorMessages;
    private UserDao userDao;
    private AppDatabase appDb;


    SignupPresenter(SignupContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        //appDb = Room.inMemoryDatabaseBuilder();
        //userDao = new UserDao();
        isSaved = true;
    }

    @Override
    public void validateForm(Map<String, String> formInput) {
        isValid = true;
        errorMessages = new ArrayList<>();

        for (HashMap.Entry<String, String> entry : formInput.entrySet()) {
            String value = entry.getValue();
            if (value.isEmpty() || value.trim().equals("") || value.length() <= 2) {
                isValid = false;
                errorMessages.add(entry.getKey() + " is required");
            }

            // Validate email format
            if (entry.getKey().equals("Email") && isValid && !value.contains("@")){
                isValid = false;
                errorMessages.add("Invalid email format");
            }
        }

        if (!isValid) {
            view.displayErrors(errorMessages);
        }
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void saveSignupData(Map<String, String> formInput) {

    }

    @Override
    public boolean isSaved() {
        return isSaved;
    }
}

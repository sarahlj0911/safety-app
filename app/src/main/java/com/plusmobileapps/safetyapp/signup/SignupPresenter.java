package com.plusmobileapps.safetyapp.signup;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Robert Beerman on 2/17/2018.
 */

public class SignupPresenter implements SignupContract.Presenter {

    public static final String NAME_INPUT = "name";
    public static final String EMAIL_INPUT = "email";
    public static final String SCHOOL_NAME_INPUT = "school_name";
    public static final String ROLE_INPUT = "role";

    private static final String TAG = "SignupPresenter";
    private SignupContract.View view;
    private boolean isSaved;
    private String errorMessage;
    private ArrayList<String> schools;

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
    public void processFormInput(Map<String, String> formInput) {
        boolean isValidInput = true;
        String name = formInput.get(NAME_INPUT);
        if (isEmpty(name)) {
            view.displayNoNameError(true);
            isValidInput = false;
        } else {
            view.displayNoNameError(false);
        }

        Log.d(TAG, "School Name is: " + formInput.get(SCHOOL_NAME_INPUT));

        String email = formInput.get(EMAIL_INPUT);
        if (isEmpty(email)) {
            view.displayNoEmailError(true);
            isValidInput = false;
        } else if (!email.contains("@")) {
            view.displayInvalidEmailError(true);
            isValidInput = false;
        } else {
            view.displayNoEmailError(false);
        }

        if (isValidInput) {
            saveSignupData(formInput);
            view.launchHomeScreen();
        }
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
        String role = formInput.get(ROLE_INPUT);


        User user = new User(1, 1, email, userName, role);

        AsyncTask<Void, Void, Boolean> saveUserTask = new SaveUserTask(user).execute();

        try {
            isSaved = saveUserTask.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(TAG, "Problem saving user");
            Log.d(TAG, e.getMessage());
            errorMessage = "Unable to save input";
        }

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        Log.d("YourMainActivity", "some more stuff");
        createUserInfoItem();
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
    public void schoolNameTextAdded() {
        view.displayNoSchoolError(false);
    }
}

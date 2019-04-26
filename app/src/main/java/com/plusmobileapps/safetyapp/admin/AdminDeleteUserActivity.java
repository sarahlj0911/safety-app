package com.plusmobileapps.safetyapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSettings;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdminDeleteUserActivity extends AppCompatActivity {
  TextView email;
    Button b1;
    CognitoUserPool userPool;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    DynamoDBMapper dynamoDBMapper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);
        userPool = new AwsServices().initAWSUserPool(this);
        //user id is the email

        //creates spinner to list users



    }
    public void backClicked(View v){
        finish();
    }
    public void onClickDelete(View v) {
        deleteUser();
        (new Handler()).postDelayed(this::deleteUser, 3000);

    }


    public void deleteUser(){
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("AAAA", "You are connected to AWS's database!");
            }
        }).execute();
        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        ArrayList<String> list = new ArrayList<String>();
        ;
        CognitoUserSettings s = new CognitoUserSettings();
        email = (findViewById(R.id.editText3));
        CognitoUser user = userPool.getUser(email.getText().toString());
        user.getSession(authenticationHandler);

        list.add("name");
        Log.d("TEST!!", email.getText().toString());
        //userPool.getUser(email.getText().toString()).deleteUser(deleteUserCallback);
        user.deleteAttributes(list, deleteUserCallback);
        user.deleteUser(deleteUserCallback);
        // userPool.getUser(email.getText().toString()).setUserSettings(s,deleteUserCallback);
        user.getDetails(hand);
        userPool = new AwsServices().initAWSUserPool(this);
        user = userPool.getUser(email.getText().toString());
        user.getDetailsInBackground(hand);
        user.getDetailsInBackground(getUserDetailsHandler);

//OR if using awsconfiguration.json
//    CognitoUserPool userPool = new CognitoUserPool(context, AWSMobileClient.getInstance().getConfiguration());

        AuthenticationDetails authDetails = new AuthenticationDetails(email.getText().toString(), "Password1%", null);


//You might want do to the following bit inside a thread as it should be done in background
        user.initiateUserAuthentication(authDetails, authenticationHandler, true).run();
        user.deleteAttributes(list, deleteUserCallback);
        user.deleteUser(deleteUserCallback);
        // userPool.getUser(email.getText().toString()).setUserSettings(s,deleteUserCallback);
        user.getDetails(hand);
        userPool = new AwsServices().initAWSUserPool(this);
        user = userPool.getUser(email.getText().toString());
        user.getDetailsInBackground(hand);
        user.deleteUserInBackground(deleteUserCallback);
    }

    GenericHandler deleteUserCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.d("SUCCESS!!","SUCCESS DELETING USER");
            Toast.makeText(AdminDeleteUserActivity.this,
                    "Successfully deleted user from the database ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("FAILURE","PROBLEM DELETING USER");
        }
    };
    GetDetailsHandler hand = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails a) {
            Log.d("SUCCESS!!","SUCESS GETTING DETAILS");

        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("FAILURE","PROBLEM GETTING DETAILS");
        }
    };
    GetDetailsHandler handler = new GetDetailsHandler() {
        @Override
        public void onSuccess(final CognitoUserDetails list) {
            // Successfully retrieved user details
        }

        @Override
        public void onFailure(final Exception exception) {
            // Failed to retrieve the user details, probe exception for the cause
        }
    };
    GetDetailsHandler getUserDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(final CognitoUserDetails list) {
            // Successfully retrieved user details
            String userName = list.getAttributes().getAttributes().get("name");
            String userRole = list.getAttributes().getAttributes().get("custom:role");
            String userSchool = list.getAttributes().getAttributes().get("custom:school");
            Log.d("!!", "Successfully loaded " +userName+ " as role " +userRole+ " at school " +userSchool);
            // userRole.equals("Administrator")
        }

        @Override
        public void onFailure(final Exception exception) {
            Log.d("asasw", "Failed to retrieve the user's details: " + exception);
        }
    };
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Log.d("AA", "User "+email.getText().toString()+" automatically signed back in");
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            Log.d("AA", "Refreshing user "+ email.getText().toString()+" details");
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            Log.d("AA", "Encountered MFA challenge. Sending user back to login...");

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.d("AA", "Encountered authentication challenge. Sending user back to login...");

        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("AA", "Unable to login user: "+exception);
        }
    };



}

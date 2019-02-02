package com.plusmobileapps.safetyapp.signup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.regions.Regions;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;

import static com.amazonaws.regions.Regions.US_EAST_1;

public class SignupConfirmActivity extends AppCompatActivity {

    private TextView descriptionText, statusText;
    boolean forcedAliasCreation = false;
    private CognitoUserPool userPool;
    private CognitoUser user;
    private TextView confirmCodeField;
    private CognitoUserAttributes userAttributes;
    private String confirmationCode, userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirm);

        descriptionText = findViewById(R.id.textViewDescription);
        statusText = findViewById(R.id.textViewStatus);
        confirmCodeField = findViewById(R.id.editTextConfirmCode);
        userEmail = getIntent().getStringExtra("userEmail");

        String description = getApplicationContext().getString(R.string.signup_confirm_description, userEmail);
        descriptionText.setText(description);
        statusText.setText("");

        String POOL_ID = "us-east-1_c0luRsLfg";
        String POOL_ARN = "arn:aws:cognito-idp:us-east-1:932483573997:userpool/us-east-1_c0luRsLfg";
        String APP_ClIENT_ID = "6qvn6fn040fd4kukil5asfc67k";
        String APP_ClIENT_SECRET = "2plp8g48dm33r6qrrf49n5mtuf90qvk05up0ah0adsefmcpb3h8";
        Regions COGNITO_REGION = US_EAST_1;
        Context CONTEXT = this;
        userPool = new CognitoUserPool(CONTEXT, POOL_ID, POOL_ARN, APP_ClIENT_SECRET, COGNITO_REGION);
        userAttributes = new CognitoUserAttributes();
    }


    public void buttonConfirmCodeClicked(View view) {
        confirmationCode = (String) confirmCodeField.getText();
        user = userPool.getUser(userEmail);
        statusText.setText("");

        // Callback Handler Confirm SignUp API
        GenericHandler confirmationCallback = new GenericHandler() {
            @Override
            public void onSuccess() {
                // TODO add user to database
                launchHomeScreen(); }

            @Override
            public void onFailure(Exception exception) {
                String status = getApplicationContext().getString(R.string.signup_status);
                statusText.setText(status); }
        };
        user.confirmSignUpInBackground(confirmationCode, forcedAliasCreation, confirmationCallback);
    }


    public void launchHomeScreen() {
        PrefManager prefManager = new PrefManager(this);
        prefManager.setIsUserSignedUp(true);
        startActivity(new Intent(SignupConfirmActivity.this, MainActivity.class));
        finish();
    }
}

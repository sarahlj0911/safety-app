package com.plusmobileapps.safetyapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.plusmobileapps.safetyapp.main.MainActivity;
import com.plusmobileapps.safetyapp.welcome.WelcomeActivity;

// TODO Move all aws from this class to the class related to the activity_signup class
public class AuthenticatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add a call to initialize AWSMobileClient
        AWSMobileClient.getInstance().initialize(this, awsStartupResult -> {
            SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(
                    AuthenticatorActivity.this, SignInUI.class);
            signin.login(AuthenticatorActivity.this, MainActivity.class).execute();
        }).execute();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

}

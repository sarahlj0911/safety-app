package com.plusmobileapps.safetyapp.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.plusmobileapps.safetyapp.R;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Added by Jeremy Powell 1/24/2019
     */
    public void buttonLogInClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Button Clicked");
        // TODO do something
    }
}

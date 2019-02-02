package com.plusmobileapps.safetyapp.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;
import com.plusmobileapps.safetyapp.signup.SignupActivity;

import java.util.HashMap;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import static com.amazonaws.regions.Regions.US_WEST_2;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    public static final String TAG = "LoginActivity";
    private String email, password;
    private TextInputLayout emailField, passwordField;
    private EditText emailInput, passwordInput;
    private LoginContract.Presenter presenter;
    private AuthenticationHandler authenticationHandler;
    private boolean validInput, validLogin;
    private CognitoUserPool userPool;
    private CircularProgressButton loginButton;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(this);
        handler = new Handler();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, 2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.buttonLogin);
        emailField = findViewById(R.id.textInputLayoutEmail);
        passwordField = findViewById(R.id.textInputLayoutPassword);

        emailInput = findViewById(R.id.fieldEmail);
        passwordInput = findViewById(R.id.fieldPassword);

        emailInput.setNextFocusDownId(R.id.fieldPassword);

        Objects.requireNonNull(emailField.getEditText()).addTextChangedListener(emailListener);
        Objects.requireNonNull(passwordField.getEditText()).addTextChangedListener(passwordListener);

        initAWSUserPool();
        createAuthenticationHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        passwordInput.setText("");
        emailInput.setText("");
        getWindow().getDecorView().clearFocus();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void displayInvalidEmailError(boolean show) {
        emailField.setError(getString(R.string.error_email_invalid));
        emailField.setErrorEnabled(show); }

    public void displayNoEmailError(boolean show) {
        emailField.setError(getString(R.string.error_email_none));
        emailField.setErrorEnabled(show); }


    public void displayNoPasswordError(boolean show) {
        passwordField.setError(getString(R.string.error_password_none));
        passwordField.setErrorEnabled(show); }


    @Override
    public void launchHomeScreen() {
        PrefManager prefManager = new PrefManager(this);
        prefManager.setIsUserSignedUp(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) { this.presenter = presenter; }


    private void initAWSUserPool(){
        String POOL_ID = "us-west-2_3KvLblMyN";
        String APP_ClIENT_ID = "1p97ciklq4r2fbapn15s0fignt";
        String APP_ClIENT_SECRET = "1ft9vtbauounq3vhlaelukluluc8qdru438iahuirqg5dscn56oh";
        Context CONTEXT = this;

        userPool = new CognitoUserPool(CONTEXT, POOL_ID, APP_ClIENT_ID, APP_ClIENT_SECRET, US_WEST_2);
    }

    private void createAuthenticationHandler(){
        authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) { validLogin = true; }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password, null);    // The API needs user sign-in credentials to continue
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);     // Pass the user sign-in credentials to the continuation
                authenticationContinuation.continueTask();  // Allow the sign-in to continue
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
                // multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
                // multiFactorAuthenticationContinuation.continueTask();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.d(TAG, "AWS Sign-in Failure: User already exists? \n" +continuation.getChallengeName());
            }

            @Override
            public void onFailure(Exception exception) {
                // Sign-in failed, check exception for the cause
                Log.d(TAG, "AWS Sign-in Failure: " +exception);
                validLogin = false; }
        };
    }


    private TextWatcher emailListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.emailTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };


    private TextWatcher passwordListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.passwordTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };


    public void buttonLogInClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Button Clicked");
        HashMap<String, String> formInput = new HashMap<>();
        emailInput.clearFocus();
        passwordInput.clearFocus();

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        formInput.put(LoginPresenter.EMAIL_INPUT, email);
        formInput.put(LoginPresenter.PASSWORD_INPUT, password);

        validInput = presenter.processFormInput(formInput);

        final Runnable successAnimation = new Runnable() { public void run() {
                loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_confirmed)); }
        };

        final Runnable failureAnimation = new Runnable() { public void run() {
            loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_failed)); }
        };

        final Runnable waitAndLaunchMain = new Runnable() { public void run() { launchHomeScreen(); } };

        final Runnable resetButton = new Runnable() { public void run() { loginButton.revertAnimation(); } };

        final Runnable resetButtonTheme = new Runnable() { public void run() {
            loginButton.setBackgroundResource(R.drawable.login_button_ripple); } };

        if (validInput) {
            loginButton.startAnimation();
            userPool.getUser(email).getSessionInBackground(authenticationHandler); // Sign in the user
            if (validLogin) {
                handler.postDelayed(successAnimation, 2000);
                handler.postDelayed(waitAndLaunchMain, 3000); }
            else {
                handler.postDelayed(failureAnimation, 2000);
                handler.postDelayed(resetButton, 5000);
                handler.postDelayed(resetButtonTheme, 5500);
            }
        }
    }


    public void buttonRegisterClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Register Clicked");
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

}
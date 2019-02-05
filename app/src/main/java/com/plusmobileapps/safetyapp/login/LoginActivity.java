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
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
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
    private boolean validLogin, showCodeView;
    private String email, password;
    private Button buttonLoginStatus, buttonDismissCodeView, buttonCode;
    private TextInputLayout emailField, passwordField;
    private EditText emailInput, passwordInput, codeInput;
    private LoginContract.Presenter presenter;
    private AuthenticationHandler authenticationHandler;
    private CognitoUserPool userPool;
    private CognitoUser user;
    private CircularProgressButton loginButton;
    private Handler handler;
    private View codeAuthWindow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(this);
        handler = new Handler();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, 2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        codeAuthWindow = findViewById(R.id.viewAuthCode);

        emailField = findViewById(R.id.textInputLayoutEmail);
        passwordField = findViewById(R.id.textInputLayoutPassword);

        buttonCode = findViewById(R.id.buttonCode);
        buttonDismissCodeView = findViewById(R.id.buttonDismissAuthCode);
        loginButton = findViewById(R.id.buttonLogin);
        buttonLoginStatus = findViewById(R.id.buttonLoginStatus);

        codeInput = findViewById(R.id.editTextCodeInput);
        emailInput = findViewById(R.id.fieldEmail);
        passwordInput = findViewById(R.id.fieldPassword);

        emailInput.setNextFocusDownId(R.id.fieldPassword);

        Objects.requireNonNull(emailField.getEditText()).addTextChangedListener(emailListener);
        Objects.requireNonNull(passwordField.getEditText()).addTextChangedListener(passwordListener);
        codeInput.addTextChangedListener(codeListener);

        codeAuthWindow.setVisibility(View.INVISIBLE);
        buttonDismissCodeView.setClickable(false);
        buttonLoginStatus.setText("");
        buttonLoginStatus.setClickable(false);

        initAWSUserPool();
        createAuthenticationHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        passwordInput.setText("");
        emailInput.setText("");
        buttonLoginStatus.setText("");
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


    private void sendCode() {
        new Thread(new Runnable() {
            public void run() {
                user.confirmSignUp(codeInput.getText().toString(), true, codeHandler); }
        }).start();
    }

    private void initAWSUserPool(){
        String POOL_ID = "us-west-2_3KvLblMyN";
        String APP_ClIENT_ID = "1p97ciklq4r2fbapn15s0fignt";
        String APP_ClIENT_SECRET = "1ft9vtbauounq3vhlaelukluluc8qdru438iahuirqg5dscn56oh";
        Context CONTEXT = this;

        userPool = new CognitoUserPool(CONTEXT, POOL_ID, APP_ClIENT_ID, APP_ClIENT_SECRET, US_WEST_2);
    }

    GenericHandler codeHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "Code was sent! Try Logging in!");
            codeAuthWindow.setVisibility(View.INVISIBLE);
            buttonDismissCodeView.setClickable(false);
            showCodeView = false; }

        @Override
        public void onFailure(Exception exception) {
            Log.d(TAG, "AWS Code Send Failure: " +exception); }
    };


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
                Log.d(TAG, "AWS Sign-in Failure: " +continuation.getChallengeName());
                if (continuation.getChallengeName().contains("NEW_PASSWORD_REQUIRED")) { // TODO change to UNCONFIRMED
                    user = userPool.getUser(email);
                    buttonLoginStatus.setText(getString(R.string.login_button_error_verify));
                    buttonLoginStatus.setClickable(true);
                    showCodeView = true;
                }
                else {
                    Log.d(TAG, "AWS Sign-in Failure: " +continuation.getChallengeName());
                    buttonLoginStatus.setText(getString(R.string.login_button_error_aws_user_exists));
                }
            }

            @Override
            public void onFailure(Exception exception) {
                // Sign-in failed, check exception for the cause
                Log.d(TAG, "AWS Sign-in Failure: " +exception);
                if (exception.toString().toLowerCase().contains("usernotfoundexception")) {
                    buttonLoginStatus.setText(getString(R.string.login_button_user_not_found)); }
                else if (exception.toString().toLowerCase().contains("notauthorizedexception")) {
                    buttonLoginStatus.setText(String.format("%s\n Reset it?", getString(R.string.login_button_error_aws_user_exists)));
                    buttonLoginStatus.setClickable(true); }
                else buttonLoginStatus.setText(String.format("%s%s", getString(R.string.login_button_error_aws_error), exception));
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
        public void onTextChanged(CharSequence s, int start, int before, int count) { presenter.passwordTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private TextWatcher codeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { buttonCode.setText("CONFIRM"); }

        @Override
        public void afterTextChanged(Editable s) { }
    };


    public void buttonLogInClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Button Clicked");
        HashMap<String, String> formInput = new HashMap<>();
        emailInput.clearFocus();
        passwordInput.clearFocus();
        buttonLoginStatus.setAlpha(0);
        buttonLoginStatus.setClickable(false);

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        formInput.put(LoginPresenter.EMAIL_INPUT, email);
        formInput.put(LoginPresenter.PASSWORD_INPUT, password);

        boolean validInput = presenter.processFormInput(formInput);

        // Button Animations
        final Runnable successAnimation = new Runnable() { public void run() {
            loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_confirmed)); }
        };

        final Runnable failureAnimation = new Runnable() { public void run() {
            if (showCodeView) { showCodeAuthorizationView(); }
            buttonLoginStatus.setAlpha(1);
            loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_failed)); }
        };

        final Runnable waitAndLaunchMain = new Runnable() { public void run() { launchHomeScreen(); } };

        final Runnable resetButton = new Runnable() { public void run() { loginButton.revertAnimation(); } };

        // Check login with AWS
        if (validInput) {
            loginButton.setBackgroundResource(R.drawable.login_button_ripple);
            loginButton.startAnimation();
            userPool.getUser(email).getSessionInBackground(authenticationHandler); // Sign in the user
            if (validLogin) {
                handler.postDelayed(successAnimation, 2000);
                // <cognitoUser>.getDetailsInBackground(getDetailsHandler);
                handler.postDelayed(waitAndLaunchMain, 3000); }
            else {
                handler.postDelayed(failureAnimation, 2000);
                handler.postDelayed(resetButton, 5000);
            }
        }
    }


    public void buttonRegisterClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Register Clicked");
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void buttonStatusClicked(View view){
        if (buttonLoginStatus.getText().toString().toLowerCase().contains("reset")){
            android.util.Log.d(TAG, "Debug: Reset Password Clicked");
            // TODO Create in page activity to prompt that password reset has been sent to user
            // use ForgotPassword API and then create a password using the delivered code
            // by calling ConfirmForgotPassword API before they sign in
        }
        else showCodeAuthorizationView();
    }

    public void buttonConfirmCodeClicked(View view){
        android.util.Log.d(TAG, "Debug: Confirm Code Button Pressed");
        if (buttonCode.getText().toString().toLowerCase().contains("confirm")) {
            sendCode();
        }
        else {
            // TODO Resend verification code
        }
    }

    public void buttonDismissAuthCodeClicked(View view) {
        codeAuthWindow.setVisibility(View.INVISIBLE);
        buttonDismissCodeView.setClickable(false);
        showCodeView = false;
    }

    public void showCodeAuthorizationView() {
        codeAuthWindow.setVisibility(View.VISIBLE);
        buttonDismissCodeView.setClickable(true);
    }

}
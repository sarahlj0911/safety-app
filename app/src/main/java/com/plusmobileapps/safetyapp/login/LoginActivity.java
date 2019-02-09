package com.plusmobileapps.safetyapp.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.BlurUtils;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;
import com.plusmobileapps.safetyapp.signup.SignupActivity;

import java.util.HashMap;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    public static final String TAG = "LoginActivity";
    private boolean showCodeView;
    private String email, password;
    private ImageView backgroundBlur;
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
    private Context CONTEXT = this;
    private AwsServices awsServices;


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
        backgroundBlur = findViewById(R.id.imageBackgroundBlur);

        codeInput = findViewById(R.id.editTextCodeInput);
        emailInput = findViewById(R.id.fieldEmail);
        passwordInput = findViewById(R.id.fieldPassword);

        emailInput.setNextFocusDownId(R.id.fieldPassword);

        Objects.requireNonNull(emailField.getEditText()).addTextChangedListener(emailListener);
        Objects.requireNonNull(passwordField.getEditText()).addTextChangedListener(passwordListener);
        codeInput.addTextChangedListener(codeListener);

        backgroundBlur.setVisibility(View.INVISIBLE);
        codeAuthWindow.setVisibility(View.INVISIBLE);
        loginButton.setBackgroundResource(R.drawable.login_button_ripple);
        buttonDismissCodeView.setClickable(false);
        buttonLoginStatus.setText("");
        buttonLoginStatus.setClickable(false);

        awsServices = new AwsServices();
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
                user.confirmSignUp(codeInput.getText().toString(), true, confirmCodeHandler);
            }
        }).start();
    }

    private void initAWSUserPool(){
        userPool = new CognitoUserPool(CONTEXT, awsServices.getPOOL_ID(), awsServices.getAPP_ClIENT_ID(), awsServices.getAPP_ClIENT_SECRET(), awsServices.getREGION());
    }

    GenericHandler confirmCodeHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "Code was sent!");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonDismissAuthCodeClicked(codeAuthWindow);
                    buttonLoginStatus.setText(R.string.login_button_user_confirmed);
                }
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onFailure(Exception exception) {
            Log.d(TAG, "AWS Code Send Failure: " +exception);
            codeAuthWindow.setBackgroundResource(R.drawable.code_authorization_fail);
            Handler handler = new Handler(getBaseContext().getMainLooper());
            handler.post( new Runnable() {
                @Override
                public void run() { buttonCode.setText("Incorrect Code"); }} );
            buttonCode.setTextColor(Color.WHITE);
            buttonCode.setBackgroundResource(R.drawable.code_confirm_button_red);}
    };


    private void createAuthenticationHandler(){
        authenticationHandler = new AuthenticationHandler() {
            // Button Animations
            final Runnable failureAnimation = new Runnable() { public void run() {
                if (showCodeView) { showCodeAuthorizationView(); }
                buttonLoginStatus.setAlpha(1);
                loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_failed)); }
            };
            final Runnable waitAndLaunchMain = new Runnable() { public void run() { launchHomeScreen(); } };
            final Runnable resetButton = new Runnable() { public void run() {
                loginButton.revertAnimation();
            } };
            final Runnable successAnimation = new Runnable() { public void run() {
                loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_confirmed)); }
            };

            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                handler.postDelayed(successAnimation, 2000);
                handler.postDelayed(waitAndLaunchMain, 3000);
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                Log.d(TAG, "AWS Sign-in: Getting Details");
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password, null);    // The API needs user sign-in credentials to continue
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);     // Pass the user sign-in credentials to the continuation
                authenticationContinuation.continueTask();  // Allow the sign-in to continue
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
                Log.d(TAG, "AWS Sign-in: Needs to use a multi-factor authentication method to sign in");
                // multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
                // multiFactorAuthenticationContinuation.continueTask();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.d(TAG, "AWS Sign-in Failure: " +continuation.getChallengeName());
                if (continuation.getChallengeName().contains("NEW_PASSWORD_REQUIRED")) {
                    user = userPool.getUser(email);
                    buttonLoginStatus.setText(getString(R.string.login_button_error_verify));
                    buttonLoginStatus.setClickable(true);
                    showCodeView = true;
                }
                else {
                    Log.d(TAG, "AWS Sign-in Failure: " +continuation.getChallengeName());
                    buttonLoginStatus.setText(getString(R.string.login_button_error_aws_user_exists));
                    handler.postDelayed(failureAnimation, 2000);
                    handler.postDelayed(resetButton, 5000);
                }
            }

            @Override
            public void onFailure(Exception exception) {
                // Sign-in failed, check exception for the cause
                String ex = exception.toString();
                Log.d(TAG, "AWS Sign-in Failure: " +ex);
                if (ex.toLowerCase().contains("usernotfoundexception")) {
                    buttonLoginStatus.setText(getString(R.string.login_button_user_not_found)); }
                else if (ex.toLowerCase().contains("notauthorizedexception")) {
                    buttonLoginStatus.setText(String.format("%s\n Reset it?", getString(R.string.login_button_error_aws_user_exists)));
                    buttonLoginStatus.setClickable(true); }
                else if (ex.toLowerCase().contains("passwordresetrequiredexception")) {
                    buttonLoginStatus.setText(getString(R.string.login_button_error_aws_new_password));
                }
                else if (ex.toLowerCase().contains("usernotconfirmedexception")) {
                    user = userPool.getUser(email);
                    buttonLoginStatus.setText(getString(R.string.login_button_error_verify));
                    buttonLoginStatus.setClickable(true);
                    showCodeView = true; }
                else buttonLoginStatus.setText(String.format("%s%s", getString(R.string.login_button_error_aws_error), exception));
                handler.postDelayed(failureAnimation, 2000);
                handler.postDelayed(resetButton, 5000); }
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (codeInput.length() == 0) codeInput.setTypeface(null, Typeface.NORMAL);
            else codeInput.setTypeface(null, Typeface.BOLD);
            codeAuthWindow.setBackgroundResource(R.drawable.code_authorization_view_layout);
            buttonCode.setTextColor(getColor(R.color.button_verification_text));
            buttonCode.setBackgroundResource(R.drawable.code_confirm_button);
            buttonCode.setText(getString(R.string.code_listener_button_confirm)); }

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

        // Check login with AWS
        if (validInput) {
            //loginButton.setBackground(Color.TRANSPARENT); TODO
            loginButton.startAnimation();
            userPool.getUser(email).getSessionInBackground(authenticationHandler); // Sign in the user
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
        //else {
            // TODO Resend verification code
        //}
    }

    public void buttonDismissAuthCodeClicked(View view) {
        hideKeyboard(view);
        backgroundBlur.setVisibility(View.INVISIBLE);
        codeAuthWindow.setVisibility(View.INVISIBLE);
        buttonDismissCodeView.setClickable(false);
        showCodeView = false;
    }

    public void showCodeAuthorizationView() {
        Bitmap backgroundCapture = takeScreenshot();
        backgroundBlur.setImageBitmap(new BlurUtils().blur(LoginActivity.this, backgroundCapture, 25f));
        backgroundBlur.setVisibility(View.VISIBLE);
        codeAuthWindow.setVisibility(View.VISIBLE);
        buttonDismissCodeView.setClickable(true);
//        codeAuthorizationView(true);
    }

    private Bitmap takeScreenshot(){
        try {
            View screen = getWindow().getDecorView().getRootView();
            screen.setDrawingCacheEnabled(true);
            return Bitmap.createBitmap(screen.getDrawingCache()); }
        catch (Throwable e) { e.printStackTrace(); return null;}
    }

    /**
     * Hides the keyboard
     * REF: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard#
     * Added by Jeremy Powell 1/24/2019
     */
    //
    private void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); }
    }

    private void codeAuthorizationView(boolean on){
        if (on) {
            Bitmap backgroundCapture = takeScreenshot();
            backgroundBlur.setImageBitmap(new BlurUtils().blur(LoginActivity.this, backgroundCapture, 25f));
            backgroundBlur.setImageAlpha(0);
            codeAuthWindow.setAlpha(0.0f);
            backgroundBlur.setVisibility(View.VISIBLE);
            codeAuthWindow.setVisibility(View.VISIBLE);
            buttonDismissCodeView.setClickable(true);
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(1000);
            anim.setRepeatCount(0);
            anim.setRepeatMode(Animation.REVERSE);
            codeAuthWindow.startAnimation(anim);
            backgroundBlur.startAnimation(anim);
        }
        else {
            backgroundBlur.setVisibility(View.INVISIBLE);
            codeAuthWindow.setVisibility(View.INVISIBLE);
            buttonDismissCodeView.setClickable(false);
            showCodeView = false;
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(1000);
            codeAuthWindow.startAnimation(anim);
            backgroundBlur.startAnimation(anim);
        }
    }

}
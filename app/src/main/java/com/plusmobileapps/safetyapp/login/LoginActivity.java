package com.plusmobileapps.safetyapp.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.BlurUtils;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;
import com.plusmobileapps.safetyapp.signup.SignupActivity;
import com.plusmobileapps.safetyapp.util.FileUtil;

import java.util.HashMap;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";
    private static final String AWSTAG = "LoginActivityAWS";
    private View codeAuthWindow, codeView, loginView;
    private Handler handler;
    private LoginContract.Presenter presenter;
    private String username, password, userName, userRole, userSchool, Email;
    private TextView appTitle, textNewUser;
    private ImageView backgroundBlur, codeBackground, buttonCodeBackground, appLogo;
    private Button buttonLoginStatus, buttonDismissCodeView, buttonCode, buttonSignUp;
    private TextInputLayout emailField, passwordField;
    private EditText emailInput, passwordInput, codeInput;
    private CircularProgressButton loginButton;
    private boolean showCodeView, hasInternetConnection;
    private int emailCharCount;
    // AWS
    private CognitoUserPool userPool;
    private CognitoUser user;

    // Login Button Animations
    final Runnable resetButton = new Runnable() {
        public void run() {
            loginButton.revertAnimation();
        }
    };
    final Runnable waitAndLaunchMain = new Runnable() {
        public void run() {
            launchHomeScreen();
        }
    };
    final Runnable successAnimation = new Runnable() {
        public void run() {
            loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_confirmed));
        }
    };
    final Runnable failureAnimation = new Runnable() {
        public void run() {
            if (showCodeView) showCodeAuthorizationView();
            loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_failed));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(this);
        handler = new Handler();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, 2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginView = findViewById(R.id.viewLogin);
        codeAuthWindow = findViewById(R.id.viewAuthCode);
        codeView = findViewById(R.id.codeView);

        emailField = findViewById(R.id.textInputLayoutEmail);
        passwordField = findViewById(R.id.textInputLayoutPassword);

        buttonCode = findViewById(R.id.buttonText);
        buttonDismissCodeView = findViewById(R.id.buttonDismissAuthCode);
        loginButton = findViewById(R.id.buttonLogin);
        buttonLoginStatus = findViewById(R.id.buttonLoginStatus);
        backgroundBlur = findViewById(R.id.imageBackgroundBlur);
        codeBackground = findViewById(R.id.viewAuthCodeBackground);
        buttonCodeBackground = findViewById(R.id.buttonCodeBackground);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        textNewUser = findViewById(R.id.textRegister);
        appTitle = findViewById(R.id.textViewTitle);
        appLogo = findViewById(R.id.imageLogo);

        codeInput = findViewById(R.id.editTextCodeInput);
        emailInput = findViewById(R.id.fieldEmail);
        passwordInput = findViewById(R.id.fieldPassword);

        Objects.requireNonNull(emailField.getEditText()).addTextChangedListener(emailListener);
        Objects.requireNonNull(passwordField.getEditText()).addTextChangedListener(passwordListener);
        codeInput.addTextChangedListener(codeListener);

        loginButton.setBackgroundResource(R.drawable.login_button_ripple);
        buttonDismissCodeView.setClickable(false);
        buttonLoginStatus.setClickable(false);

        userPool = new AwsServices().initAWSUserPool(this);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
            }
        }).execute();
        FileUtil.download(this, "schools.json", "/data/data/com.plusmobileapps.safetyapp/databases/schools.json");


    }

    @Override
    protected void onResume() {
        super.onResume();
        String openAnimation = getIntent().getStringExtra("openAni");

        passwordInput.setText("");
        emailInput.setText("");
        buttonLoginStatus.setText("");
        getWindow().getDecorView().clearFocus();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            hasInternetConnection = true;
            buttonSignUp.setEnabled(true);
            loginButton.setEnabled(true);
            buttonLoginStatus.setText(""); }
        else {
            hasInternetConnection = false;
            buttonSignUp.setEnabled(false);
            loginButton.setEnabled(false);
            buttonLoginStatus.setText("No Internet Connection!\nApp services will be unavailable.");
            Log.e(TAG, "Device does not have an internet connection!"); }

        if (openAnimation != null && openAnimation.equals("start")) activityStartingAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginButton.dispose();
    }

    @Override
    public void launchHomeScreen() {
//        PrefManager prefManager = new PrefManager(this);
//        prefManager.setIsUserSignedUp(true);
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        mainActivity.putExtra("activity", "from login");
        mainActivity.putExtra("email", username);
        mainActivity.putExtra("name", userName);
        mainActivity.putExtra("role", userRole);
        mainActivity.putExtra("school", userSchool);
        Log.d(TAG,"Sending Info to main activity "+userName+" "+username+" "+userRole+" "+userSchool);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }


    public void displayInvalidEmailError(boolean show) {
        emailField.setError(getString(R.string.error_email_invalid));
        emailField.setErrorEnabled(show);
    }

    public void displayNoEmailError(boolean show) {
        emailField.setError(getString(R.string.error_email_none));
        emailField.setErrorEnabled(show);
    }

    public void displayNoPasswordError(boolean show) {
        passwordField.setError(getString(R.string.error_password_none));
        passwordField.setErrorEnabled(show);
    }




    // Handler: Login User
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        // Button Animations


        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Log.d(AWSTAG, "Login successful");
            user = userPool.getUser(username);
            user.getDetailsInBackground(getUserDetailsHandler);
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            Log.d(AWSTAG, "Sign-in: Getting Details");
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password, null);    // The API needs user sign-in credentials to continue
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);     // Pass the user sign-in credentials to the continuation
            authenticationContinuation.continueTask();  // Allow the sign-in to continue
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            Log.d(AWSTAG, "Sign-in: Using multi-factor authentication");
            // multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // multiFactorAuthenticationContinuation.continueTask();
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.d(AWSTAG, "Sign-in Challenge: " + continuation.getChallengeName());
            if (continuation.getChallengeName().contains("NEW_PASSWORD_REQUIRED")) {
                user = userPool.getUser(username);
                showCodeView = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_error_verify));
                        buttonLoginStatus.setClickable(true);
                    }
                }); }
            else { runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonLoginStatus.setText(getString(R.string.login_button_error_aws_user_exists));
                    handler.postDelayed(failureAnimation, 0);
                    handler.postDelayed(resetButton, 3000);
                }
            });
            }
        }

        @Override
        public void onFailure(Exception exception) {
            String ex = exception.toString();
            String buttonErrorText;
            Log.d(TAG, "AWS Sign-in Failure: " + ex);
            if (!ex.toLowerCase().contains("cognitointernalerrorexception")) {
                if (ex.toLowerCase().contains("usernotfoundexception")) {
                    buttonErrorText = getString(R.string.login_button_user_not_found);
                }
                else if (ex.toLowerCase().contains("user is disabled")) {
                    buttonErrorText = getString(R.string.aws_login_error_disabled);
                    buttonLoginStatus.setClickable(true);
                }
                else if (ex.toLowerCase().contains("password attempts exceeded")) {
                    buttonErrorText = getString(R.string.login_button_error_aws_attempts_exceeded);
                    buttonLoginStatus.setClickable(true);
                }
                else if (ex.toLowerCase().contains("incorrect username or password")) {
                    buttonErrorText = String.format("%s\n Reset it?", getString(R.string.login_button_error_aws_user_exists));
                    buttonLoginStatus.setClickable(true);
                }
                else if (ex.toLowerCase().contains("passwordresetrequiredexception")) {
                    buttonErrorText = getString(R.string.login_button_error_aws_new_password);
                    //TODO launch reset password dialog
                }
                else if (ex.toLowerCase().contains("usernotconfirmedexception")) {
                    buttonErrorText = getString(R.string.login_button_error_verify);
                    user = userPool.getUser(username);
                    buttonLoginStatus.setClickable(true);
                    showCodeView = true;
                }
                else if (ex.toLowerCase().contains("unable to resolve host")) {
                    buttonErrorText = getString(R.string.login_button_error_AWS_connection_issue);
                }
                else {
                    buttonErrorText = getString(R.string.login_button_error_aws_error);
                }
                final String finalButtonErrorText = buttonErrorText;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(finalButtonErrorText);
                        buttonLoginStatus.setAlpha(1);
                    }
                });
            }
            handler.postDelayed(failureAnimation, 0);
            handler.postDelayed(resetButton, 3000);
        }
    };

    // Handler: Code Confirmation
    GenericHandler confirmCodeHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.d(AWSTAG, "Code was sent!");
            showCodeView = true;
            final Runnable clearView = new Runnable() {
                public void run() {
                    buttonDismissAuthCodeClicked(codeAuthWindow);
                    buttonLogInClicked(loginView); // TODO
                    runOnUiThread(() -> {
                        buttonLoginStatus.setText(R.string.login_button_user_confirmed);
                        buttonLoginStatus.setClickable(false);
                    });
                }
            };
            Handler viewHandler = new Handler(getBaseContext().getMainLooper());
            codeViewStatusAnimation(0, 200, "CONFIRMED");
            viewHandler.postDelayed(clearView, 2000);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onFailure(Exception exception) {
            String ex = exception.toString();
            String buttonErrorText;
            Log.d(AWSTAG, "AWS Code Authentication Failure: " + ex);
            if (ex.toLowerCase().contains("codemismatchexception")) {
                buttonErrorText = getString(R.string.login_button_error_aws_code_error_incorrect);
            }
            else if (ex.toLowerCase().contains("limitexceededexception")) {
                buttonErrorText = getString(R.string.login_button_error_aws_code_error_limit);
            }
            else {
                buttonErrorText = getString(R.string.login_button_error_aws_code_error_default);
            }
            codeViewStatusAnimation(1, 200, buttonErrorText.toUpperCase());
            codeViewErrorAnimation();
        }
    };

    // Handler: Resend Confirmation Code
    VerificationHandler resendConfirmationCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
            Log.d(AWSTAG, "Resend Code Successful");
            codeViewStatusAnimation(0, 200, "CODE SENT");
        }

        @Override
        public void onFailure(Exception ex) {
            String showErr;
            Log.d(AWSTAG, "Resend Code Failure: " + ex);
            if (ex.toString().toLowerCase().contains("limitexceededexception"))
                showErr = "ATTEMPT LIMIT EXCEEDED";
            else showErr = "ISSUE SENDING CODE";
            codeViewStatusAnimation(1, 200, showErr);
        }
    };

    // Handler: Get User Details
    GetDetailsHandler getUserDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(final CognitoUserDetails list) {
            // Successfully retrieved user details
            userName = list.getAttributes().getAttributes().get("name");
            userRole = list.getAttributes().getAttributes().get("custom:role");
            userSchool = list.getAttributes().getAttributes().get("custom:school");

            Log.d(AWSTAG, "Successfully loaded " +userName+ " as role " +userRole+ " at school " +userSchool);

            handler.postDelayed(successAnimation, 0);
            handler.postDelayed(waitAndLaunchMain, 300);
        }

        @Override
        public void onFailure(final Exception exception) {
            Log.d(AWSTAG, "Failed to retrieve the user's details: " + exception);
            final String finalButtonErrorText = getString(R.string.aws_login_error_details);
            runOnUiThread(() -> {
                buttonLoginStatus.setText(finalButtonErrorText);
                buttonLoginStatus.setAlpha(1);
            });
            handler.postDelayed(failureAnimation, 0);
            handler.postDelayed(resetButton, 3000);
        }
    };


    private TextWatcher emailListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int charCount = emailInput.getText().length();
            if (charCount > emailCharCount + 1) passwordInput.requestFocus();
            emailCharCount = charCount;
            presenter.emailTextAdded();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher passwordListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.passwordTextAdded();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher codeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            runOnUiThread(() -> {
                if (codeInput.length() == 0) codeInput.setTypeface(null, Typeface.NORMAL);
                else codeInput.setTypeface(null, Typeface.BOLD);
                codeViewStatusAnimation(2, 200, "CONFIRM");
            });
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    public void buttonLogInClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Button Clicked");
        HashMap<String, String> formInput = new HashMap<>();
        emailInput.clearFocus();
        passwordInput.clearFocus();
        buttonLoginStatus.setAlpha(0);
        buttonLoginStatus.setClickable(false);

        username = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        formInput.put(LoginPresenter.EMAIL_INPUT, username);
        formInput.put(LoginPresenter.PASSWORD_INPUT, password);

        boolean validInput = presenter.processFormInput(formInput);

        // Check login with AWS
        if (validInput & hasInternetConnection) {
            loginButton.startAnimation();
            codeInput.setText("");
            codeViewStatusAnimation(3, 0, "RESEND CODE");
            userPool.getUser(username).getSessionInBackground(authenticationHandler); // Sign in the user
        }
    }

    public void buttonSignUpClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Register Clicked");
        Intent signUp = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(signUp);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void buttonStatusClicked(View view) {
        if (buttonLoginStatus.getText().toString().toLowerCase().contains("reset")) {
            android.util.Log.d(TAG, "Debug: Reset Password Clicked");
            // TODO Create in page activity to prompt that password reset has been sent to user
            // use ForgotPassword API and then create a password using the delivered code
            // by calling ConfirmForgotPassword API before they sign in
        } else showCodeAuthorizationView();
    }

    public void buttonConfirmCodeClicked(View view) {
        android.util.Log.d(TAG, "Debug: Confirm Code Button Pressed");
        codeInput.clearFocus();
        if (buttonCode.getText().toString().toLowerCase().contains("confirm")) {
            new Thread(() -> user.confirmSignUp(codeInput.getText().toString(), true, confirmCodeHandler)).start(); }
        else if (buttonCode.getText().toString().toLowerCase().contains("resend")) {
            new Thread(() -> user.resendConfirmationCode(resendConfirmationCodeHandler)).start();
        }
    }

    public void buttonDismissAuthCodeClicked(View view) {
        hideKeyboard(view);
        showCodeView = false;
        buttonDismissCodeView.setClickable(false);
        runOnUiThread(() -> codeViewShowAnimation(false, 100));
    }

    public void showCodeAuthorizationView() {
        buttonDismissCodeView.setClickable(true);
        runOnUiThread(() -> {
            Bitmap backgroundCapture = takeScreenshot();
            backgroundBlur.setImageBitmap(new BlurUtils().blur(LoginActivity.this, backgroundCapture, 25f));
            codeViewShowAnimation(true, 250);
        });
    }

    private Bitmap takeScreenshot() {
        try {
            View screen = getWindow().getDecorView().getRootView();
            screen.setDrawingCacheEnabled(true);
            return Bitmap.createBitmap(screen.getDrawingCache()); }
        catch (Throwable e) {
            e.printStackTrace();
            return null; }
    }

    /**
     * Hides the keyboard
     * REF: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard#
     */
    private void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); }
    }

    private void codeViewShowAnimation(Boolean show, int duration) {
        AnimationSet animations = new AnimationSet(false);
        animations.setRepeatMode(0);

        if (show) {
            codeView.setVisibility(View.VISIBLE);
            AlphaAnimation fadeInAni = new AlphaAnimation(0.0f, 1.0f);
            fadeInAni.setInterpolator(new AccelerateInterpolator());
            fadeInAni.setDuration(duration);
            animations.addAnimation(fadeInAni);

            Animation scaleInAni = new ScaleAnimation(0.97f, 1f, 0.97f, 1f, codeAuthWindow.getWidth() / 2f, codeAuthWindow.getHeight() / 2f);
            scaleInAni.setDuration(1000);
            scaleInAni.setInterpolator(new DecelerateInterpolator());
            codeAuthWindow.startAnimation(scaleInAni); }
        else {
            AlphaAnimation fadeOutAni = new AlphaAnimation(1.0f, 0.0f);
            fadeOutAni.setInterpolator(new AccelerateInterpolator());
            fadeOutAni.setDuration(duration);
            fadeOutAni.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    codeView.setVisibility(View.INVISIBLE); }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            animations.addAnimation(fadeOutAni);

            Animation scaleOutAni = new ScaleAnimation(1f, 0.97f, 1f, 0.97f, codeAuthWindow.getWidth() / 2f, codeAuthWindow.getHeight() / 2f);
            scaleOutAni.setDuration(duration);
            scaleOutAni.setInterpolator(new AccelerateDecelerateInterpolator());
            codeAuthWindow.startAnimation(scaleOutAni); }
        codeView.startAnimation(animations);
    }

    private void codeViewStatusAnimation(int status, final int duration, final String buttonText) {
        final TransitionDrawable backgroundTransition, buttonTransition;
        final boolean reverse;

        if (status == 1) { // FAILURE
            reverse = false;
            buttonTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_button_fail_animation);
            backgroundTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_fail_animation); }
        else {  // RESET
            reverse = status > 1;
            buttonTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_button_success_animation);
            backgroundTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_success_animation); }

        runOnUiThread(() -> {
            buttonCode.setText(buttonText);
            buttonCodeBackground.setImageDrawable(buttonTransition);
            codeBackground.setImageDrawable(backgroundTransition);
            if (reverse) {
                buttonCode.setTextColor(getColor(R.color.button_verification_text));
                Objects.requireNonNull(buttonTransition).resetTransition();
                Objects.requireNonNull(backgroundTransition).resetTransition();

                Handler handler = new Handler(getBaseContext().getMainLooper());
                final Runnable resetButton = new Runnable() {
                    public void run() {
                        buttonCode.setBackgroundResource(R.drawable.rounded_button_animation); }};
                handler.postDelayed(resetButton, duration); }
            else {
                buttonCode.setTextColor(Color.WHITE);
                buttonCode.setBackgroundResource(0);
                Objects.requireNonNull(buttonTransition).startTransition(duration);
                Objects.requireNonNull(backgroundTransition).startTransition(duration); }
        });
    }

    private void codeViewErrorAnimation() {
        Animation shakeViewAni = AnimationUtils.loadAnimation(this, R.anim.shake_horizontally);
        codeAuthWindow.startAnimation(shakeViewAni);
    }

    private void activityStartingAnimation() {
        Animation fadeIn, logoStart, emailFieldAni, passwordFieldAni, newUserAni, signUpAni, statusAni, buttonAni;
        int fieldOffset, fieldOffsetPlus;

        fieldOffset = 500;
        fieldOffsetPlus = 85;

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(100);
        fadeIn.setStartOffset(900);

        logoStart = AnimationUtils.loadAnimation(this, R.anim.logo_start_animation);
        emailFieldAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        passwordFieldAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        newUserAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        signUpAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        statusAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        buttonAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);

        emailFieldAni.setStartOffset(fieldOffset + fieldOffsetPlus*2);
        passwordFieldAni.setStartOffset(fieldOffset + fieldOffsetPlus*3);
        newUserAni.setStartOffset(fieldOffset + fieldOffsetPlus*4);
        signUpAni.setStartOffset(fieldOffset + fieldOffsetPlus*4);
        statusAni.setStartOffset(fieldOffset + fieldOffsetPlus*5);
        buttonAni.setStartOffset(fieldOffset + fieldOffsetPlus*6);

        appLogo.startAnimation(logoStart);
        appTitle.startAnimation(fadeIn);
        emailField.startAnimation(emailFieldAni);
        passwordField.startAnimation(passwordFieldAni);
        textNewUser.startAnimation(newUserAni);
        buttonSignUp.startAnimation(signUpAni);
        buttonLoginStatus.startAnimation(statusAni);
        loginButton.startAnimation(buttonAni);

    }
}
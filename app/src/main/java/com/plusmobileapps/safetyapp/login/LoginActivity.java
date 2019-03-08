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

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
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
    private View codeAuthWindow, codeView;
    private Bundle fadeOutActivity;
    private Handler handler;
    private LoginContract.Presenter presenter;
    private String email, password;
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
        codeView = findViewById(R.id.codeView);

        emailField = findViewById(R.id.textInputLayoutEmail);
        passwordField = findViewById(R.id.textInputLayoutPassword);

        buttonCode = findViewById(R.id.buttonCode);
        buttonDismissCodeView = findViewById(R.id.buttonDismissAuthCode);
        loginButton = findViewById(R.id.buttonLogin);
        buttonLoginStatus = findViewById(R.id.buttonLoginStatus);
        backgroundBlur = findViewById(R.id.imageBackgroundBlur);
        codeBackground = findViewById(R.id.viewAuthCodeBackground);
        buttonCodeBackground = findViewById(R.id.buttonCodeBackground);
        buttonSignUp = findViewById(R.id.buttonRegister);

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

//        logoStart = AnimationUtils.loadAnimation(this, R.animator.logo_start);
        fadeOutActivity = ActivityOptionsCompat.makeCustomAnimation(this, 1, android.R.anim.fade_out).toBundle();

        userPool = new AwsServices().initAWSUserPool(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String openAnimation = intent.getStringExtra("openAni");

        passwordInput.setText("");
        emailInput.setText("");
        buttonLoginStatus.setText("");
        getWindow().getDecorView().clearFocus();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            hasInternetConnection = true;
            buttonLoginStatus.setText("");
        } else {
            hasInternetConnection = false;
            buttonLoginStatus.setText("You have no internet connection!\nApp services will be unavailable.");
            Log.e(TAG, "Device does not have an internet connection!");
        }

        if (openAnimation.equals("start")) activityStartingAnimation("start");
//        else if (openAnimation.equals("back")) {
//            activityStartingAnimation("back"); }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginButton.dispose();
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


    @Override
    public void launchHomeScreen() {
        PrefManager prefManager = new PrefManager(this);
        prefManager.setIsUserSignedUp(true);
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        mainActivity.putExtra("email", email);
        startActivity(mainActivity, fadeOutActivity);
        finish();
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }


    // Handler: Login User
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        // Button Animations
        final Runnable successAnimation = new Runnable() {
            public void run() {
                loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_confirmed));
            }
        };
        final Runnable failureAnimation = new Runnable() {
            public void run() {
                if (showCodeView) {
                    showCodeAuthorizationView();
                }
                buttonLoginStatus.setAlpha(1);
                loginButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_failed));
            }
        };
        final Runnable waitAndLaunchMain = new Runnable() {
            public void run() {
                launchHomeScreen();
            }
        };
        final Runnable resetButton = new Runnable() {
            public void run() {
                loginButton.revertAnimation();
            }
        };


        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            handler.postDelayed(successAnimation, 500);
            handler.postDelayed(waitAndLaunchMain, 1500);
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
            Log.d(TAG, "AWS Sign-in Challenge: " + continuation.getChallengeName());
            if (continuation.getChallengeName().contains("NEW_PASSWORD_REQUIRED")) {
                user = userPool.getUser(email);
                showCodeView = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_error_verify));
                        buttonLoginStatus.setClickable(true);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_error_aws_user_exists));
                        handler.postDelayed(failureAnimation, 2000);
                        handler.postDelayed(resetButton, 5000);
                    }
                });
            }
        }

        @Override
        public void onFailure(Exception exception) {
            String ex = exception.toString();
            Log.d(TAG, "AWS Sign-in Failure: " + ex);
            if (ex.toLowerCase().contains("usernotfoundexception")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_user_not_found));
                    }
                });
            } else if (ex.toLowerCase().contains("user is disabled")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.aws_login_error_disabled));
                        buttonLoginStatus.setClickable(true);
                    }
                });
            } else if (ex.toLowerCase().contains("notauthorizedexception")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(String.format("%s\n Reset it?", getString(R.string.login_button_error_aws_user_exists)));
                        buttonLoginStatus.setClickable(true);
                    }
                });
            } else if (ex.toLowerCase().contains("passwordresetrequiredexception")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_error_aws_new_password));
                    }
                });
            } else if (ex.toLowerCase().contains("usernotconfirmedexception")) {
                user = userPool.getUser(email);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_error_verify));
                        buttonLoginStatus.setClickable(true);
                        showCodeView = true;
                    }
                });
            } else if (ex.toLowerCase().contains("unable to resolve host")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(getString(R.string.login_button_error_AWS_connection_issue));
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonLoginStatus.setText(R.string.login_button_error_aws_error);
                    }
                });
            }
            handler.postDelayed(failureAnimation, 500);
            handler.postDelayed(resetButton, 3000);
        }
    };

    // Handler: Code Confirmation
    GenericHandler confirmCodeHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "Code was sent!");
            final Runnable clearView = new Runnable() {
                public void run() {
                    buttonDismissAuthCodeClicked(codeAuthWindow);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonLoginStatus.setText(R.string.login_button_user_confirmed);
                            buttonLoginStatus.setClickable(false);
                        }
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
            Log.d(TAG, "AWS Code Send Failure: " + exception);
            codeViewStatusAnimation(1, 200, "INCORRECT CODE");
        }
    };

    // Handler: Resend Confirmation Code
    VerificationHandler resendConfirmationCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
            codeViewStatusAnimation(0, 200, "CODE SENT");
        }

        @Override
        public void onFailure(Exception ex) {
            String showErr;
            Log.d(TAG, "AWS Code Email Send Failure: " + ex);
            if (ex.toString().toLowerCase().contains("limitexceededexception"))
                showErr = "ATTEMPT LIMIT EXCEEDED";
            else showErr = "ISSUE SENDING CODE";
            codeViewStatusAnimation(1, 200, showErr);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (codeInput.length() == 0) codeInput.setTypeface(null, Typeface.NORMAL);
                    else codeInput.setTypeface(null, Typeface.BOLD);
                    codeViewStatusAnimation(2, 200, "CONFIRM");
                }
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

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        formInput.put(LoginPresenter.EMAIL_INPUT, email);
        formInput.put(LoginPresenter.PASSWORD_INPUT, password);

        boolean validInput = presenter.processFormInput(formInput);

        // Check login with AWS
        if (validInput & hasInternetConnection) {
            loginButton.startAnimation();
            codeInput.setText("");
            codeViewStatusAnimation(3, 0, "RESEND CODE");
            userPool.getUser(email).getSessionInBackground(authenticationHandler); // Sign in the user
        }
    }

    public void buttonRegisterClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Register Clicked");
        Intent signUp = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(signUp, fadeOutActivity);
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
            new Thread(new Runnable() {
                public void run() {
                    user.confirmSignUp(codeInput.getText().toString(), true, confirmCodeHandler);
                }
            }).start();
        } else if (buttonCode.getText().toString().toLowerCase().contains("resend")) {
            new Thread(new Runnable() {
                public void run() {
                    user.resendConfirmationCode(resendConfirmationCodeHandler);
                }
            }).start();
        }
    }

    public void buttonDismissAuthCodeClicked(View view) {
        hideKeyboard(view);
        showCodeView = false;
        buttonDismissCodeView.setClickable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                codeViewShowAnimation(false, 100);
            }
        });
    }

    public void showCodeAuthorizationView() {
        buttonDismissCodeView.setClickable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap backgroundCapture = takeScreenshot();
                backgroundBlur.setImageBitmap(new BlurUtils().blur(LoginActivity.this, backgroundCapture, 25f));
                codeViewShowAnimation(true, 250);
            }
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
            animations.addAnimation(fadeOutAni); }
        codeView.startAnimation(animations);
    }


    private void codeViewStatusAnimation(int status, final int duration, final String buttonText) {
        final TransitionDrawable backgroundTransition, buttonTransition;
        final boolean reverse;

        if (status == 1) { // FAILURE
            reverse = false;
            buttonTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_button_fail_animation);
            backgroundTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_fail_animation); }
        else { // SUCCESS(0) or RESET(>1)
            reverse = status > 1;
            buttonTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_button_success_animation);
            backgroundTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.code_authorization_success_animation); }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                            buttonCode.setBackgroundResource(R.drawable.rounded_button_animation);
                        }
                    };
                    handler.postDelayed(resetButton, duration); }
                else {
                    buttonCode.setTextColor(Color.WHITE);
                    buttonCode.setBackgroundResource(0);
                    Objects.requireNonNull(buttonTransition).startTransition(duration);
                    Objects.requireNonNull(backgroundTransition).startTransition(duration); }
            }
        });
    }


    private void activityStartingAnimation(String type) {
        // TODO animations for buttonLoginStatus, emailField, passwordField, appLogo, appTitle
        Animation fadeIn, logoStart, emailFieldAni, passwordFieldAni, newUserAni, signUpAni, statusAni, buttonAni;
        int fieldOffset, fieldOffsetPlus;

        fieldOffset = 1300;
        fieldOffsetPlus = 120;

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(100);
        fadeIn.setStartOffset(1700);

        logoStart = AnimationUtils.loadAnimation(this, R.anim.logo_start_animation);
        emailFieldAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        passwordFieldAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        newUserAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        signUpAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        statusAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
        buttonAni = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);

//        if (type.equals("start")) {
//            logoStart = AnimationUtils.loadAnimation(this, R.anim.logo_start_animation);
//            fieldOffset = 1300;
//            fieldOffsetPlus = 120;}
//        else {
//            logoStart = AnimationUtils.loadAnimation(this, R.anim.fade_move_up_animation);
//            fieldOffset = 100;
//            fieldOffsetPlus = 50;
//            int newDuration = 700;
//            logoStart.setStartOffset(fieldOffset + fieldOffsetPlus);
//            logoStart.setDuration(newDuration);
//            emailFieldAni.setDuration(newDuration);
//            passwordFieldAni.setDuration(newDuration);
//            newUserAni.setDuration(newDuration);
//            signUpAni.setDuration(newDuration);
//            statusAni.setDuration(newDuration);
//            buttonAni.setDuration(newDuration);
//        }

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

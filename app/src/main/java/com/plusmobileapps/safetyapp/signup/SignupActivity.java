package com.plusmobileapps.safetyapp.signup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.BlurUtils;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.login.LoginActivity;
import com.plusmobileapps.safetyapp.main.MainActivity;
import com.plusmobileapps.safetyapp.util.FileUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;
    private View customPopup, customPopupWindow;
    private Bitmap backgroundCapture;
    private ImageView customPopupBackground;
    private TextInputLayout nameInput, emailInput, passwordInput, passwordCheckInput, schoolInput;
    private TextView statusText, alertContent;
    private Spinner schoolSpinner, roleSpinner;
    private CircularProgressButton signUpButton;
    private EditText newSchool, nameField, emailField, passwordField;
    private boolean userSignedUp = false;
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;
    private Handler handler;
    private String email, name;
    private int nameCharCount, emailCharCount;

    public ArrayAdapter<String> schoolsAdapter;
    List<String> schoolNames = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Debug"+TAG, "onCreate");
        super.onCreate(savedInstanceState);

        handler = new Handler();
        presenter = new SignupPresenter(this);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, 2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        schoolSpinner = findViewById(R.id.spinner_signup_school_name);
        roleSpinner = findViewById(R.id.spinner_signup_role);

        newSchool = findViewById(R.id.new_school_text_box);
        signUpButton = findViewById(R.id.button_save_signup);
        signUpButton.setOnClickListener(saveSignupClickListener);
        signUpButton.setBackgroundResource(R.drawable.login_button_ripple);

        nameInput = findViewById(R.id.signup_name);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);
        passwordCheckInput = findViewById(R.id.signup_password_check);
        newSchool = findViewById(R.id.new_school_text_box);
        nameField = findViewById(R.id.fieldName);
        emailField = findViewById(R.id.fieldEmail);
        passwordField = findViewById(R.id.fieldPassword);
        statusText = findViewById(R.id.textViewStatus);

        ViewGroup.LayoutParams defaultLayoutParams = findViewById(R.id.viewSignup).getLayoutParams();
        customPopup = getLayoutInflater().inflate(R.layout.custom_popup, null);
        customPopup.setVisibility(View.INVISIBLE);
        addContentView(customPopup, defaultLayoutParams);
        customPopupWindow = findViewById(R.id.viewAlertWindow);
        customPopupBackground = findViewById(R.id.imageBackgroundBlur);
        TextView alertTitle = findViewById(R.id.textTitle);
        alertContent = findViewById(R.id.textDescription);
        TextView alertButton = findViewById(R.id.buttonText);

        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(nameListener);
        Objects.requireNonNull(emailInput.getEditText()).addTextChangedListener(emailListener);
        Objects.requireNonNull(passwordInput.getEditText()).addTextChangedListener(passwordListener);
        Objects.requireNonNull(passwordCheckInput.getEditText()).addTextChangedListener(passwordCheckListener);

        statusText.setText("");
        alertTitle.setText(getString(R.string.signup_confirm_title));
        alertButton.setText(getString(R.string.signup_button_inbox));

        Schools.load(this.getApplicationContext());
        schoolNames = Schools.schoolNames;

        schoolsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                schoolNames );
        schoolsAdapter.setDropDownViewResource(R.layout.activity_signup_spinner_dropdown);
        schoolSpinner.setAdapter(schoolsAdapter);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.signup_roles, R.layout.activity_signup_spinner);
        adapter.setDropDownViewResource(R.layout.activity_signup_spinner_dropdown);
        roleSpinner.setAdapter(adapter);

        userPool = new AwsServices().initAWSUserPool(this);
        userAttributes = new CognitoUserAttributes();
        initSchoolSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug"+TAG, "onResume");
        if (userSignedUp) launchLoginScreen();
        else presenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signUpButton.dispose();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchLoginScreen();
    }

    public void setPresenter(SignupContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void launchHomeScreen() {
        Intent signUp = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(signUp);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void populateSchoolSpinner(ArrayList<String> schools) { }

    public void launchLoginScreen() {
        Intent login = new Intent(SignupActivity.this, LoginActivity.class);
        login.putExtra("openAni", "back");
        startActivity(login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void initSchoolSpinner() {

    }


    private View.OnClickListener saveSignupClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            HashMap<String, String> formInput = new HashMap<>();
            statusText.setText("");
            nameInput.clearFocus();
            emailInput.clearFocus();
            passwordInput.clearFocus();
            passwordCheckInput.clearFocus();

            name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();
            email = Objects.requireNonNull(emailInput.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordInput.getEditText()).getText().toString();
            String passwordCheck = Objects.requireNonNull(passwordCheckInput.getEditText()).getText().toString();

            String role = roleSpinner.getSelectedItem().toString();

            String school = schoolSpinner.getSelectedItem().toString();

            formInput.put(SignupPresenter.NAME_INPUT, name);
            formInput.put(SignupPresenter.EMAIL_INPUT, email);
            formInput.put(SignupPresenter.PASSWORD_INPUT, password);
            formInput.put(SignupPresenter.PASSWORD_INPUT_CHECK, passwordCheck);
            formInput.put(SignupPresenter.SCHOOL_NAME_INPUT, school);
            formInput.put(SignupPresenter.ROLE_INPUT, role);

            Boolean inputReady = presenter.processFormInput(formInput);

            if (inputReady) { // Call AWS
                signUpButton.startAnimation();
                userAttributes.addAttribute("name", name);
                userAttributes.addAttribute("email", email);
                userAttributes.addAttribute("custom:role", role);
                userAttributes.addAttribute("custom:school", school);
                userPool.signUpInBackground(email, password, userAttributes, null, signupCallback);
            }
        }
    };

    @Override
    public void displayInvalidEmailError(boolean show) {
        emailInput.setError(getString(R.string.error_email_invalid));
        emailInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoEmailError(boolean show) {
        emailInput.setError(getString(R.string.error_email_none));
        emailInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoNameError(boolean show) {
        nameInput.setError(getString(R.string.error_name));
        nameInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoSchoolError(boolean show) { // TODO implement school list
//        schoolNameInput.setError(getString(R.string.error_school_name));
//        schoolNameInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoPasswordError(boolean show) {
        passwordInput.setError(getString(R.string.error_no_password));
        passwordInput.setErrorEnabled(show);
    }

    @Override
    public void displayInvalidPasswordError(boolean show) {
        passwordInput.setError(getString(R.string.error_invalid_password));
        passwordInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoPasswordCheckError(boolean show) {
        passwordCheckInput.setError(getString(R.string.error_no_password));
        passwordCheckInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoPasswordCheckErrorNoMatch(boolean show) {
        passwordCheckInput.setError(getString(R.string.error_invalid_password_no_match));
        passwordCheckInput.setErrorEnabled(show);
    }

    @Override
    public void displayInvalidPasswordLengthError(boolean show) {
        passwordInput.setError(getString(R.string.error_invalid_password_length));
        passwordInput.setErrorEnabled(show);
    }


    private TextWatcher nameListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int charCount = nameField.getText().length();
            if (charCount > nameCharCount+1) emailField.requestFocus();
            nameCharCount = charCount;
            presenter.nameTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private TextWatcher emailListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int charCount = emailField.getText().length();
            if (charCount > emailCharCount+1) passwordField.requestFocus();
            emailCharCount = charCount;
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

    private TextWatcher passwordCheckListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.passwordCheckTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private TextWatcher schoolNameListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.schoolNameTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };


    // Handler: Signup User
    SignUpHandler signupCallback = new SignUpHandler() {
        // Button Animations
        final Runnable successAnimation = new Runnable() { public void run() {
            signUpButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_confirmed)); }
        };
        final Runnable failureAnimation = new Runnable() { public void run() {
            signUpButton.doneLoadingAnimation(Color.parseColor("#ffffff"), BitmapFactory.decodeResource(getResources(), R.drawable.login_button_failed)); }
        };
        final Runnable resetButton = new Runnable() { public void run() {
            signUpButton.revertAnimation();
        } };
        final Runnable waitAndLaunchDialog = new Runnable() { public void run() {
            userSignedUp = true;
            backgroundCapture = takeScreenshot();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPopupView();
                }
            });
        } };

        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            statusText.setText("");
            if (!signUpConfirmationState) { // User confirming via email code
                handler.postDelayed(successAnimation, 0);
                handler.postDelayed(waitAndLaunchDialog, 500);
            }
            else launchHomeScreen();
        }

        @Override
        public void onFailure(Exception exception) {
            String e = exception.toString();
            Log.d(TAG, "User sign-up failed: " + e);
            String error;
            if (e.toLowerCase().contains("constraint: member")) {
                error = "Password must be at least 8 characters";
            } else if (e.toLowerCase().contains("policy:")) {
                error = StringUtils.substringBetween(e, "policy:", " (Service");
            } else if (e.toLowerCase().contains("usernameexistsexception")) {
                error = StringUtils.substringBetween(e, "UsernameExistsException:", " (Service");
            } else error = getApplicationContext().getString(R.string.status_text, name);
            statusText.setText(error);
            handler.postDelayed(failureAnimation, 0);
            handler.postDelayed(resetButton, 3000);
        }
    };


    public void showPopupView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertContent.setText(String.format("A confirmation email has been sent to %s.", email));
                customPopupBackground.setImageBitmap(new BlurUtils().blur(SignupActivity.this, backgroundCapture, 25f));
                codeViewShowAnimation(true, 250);
            }
        });
    }

    public void popupButtonClicked(View view) {
        buttonDismissViewClicked(customPopup);
        Intent intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void buttonDismissViewClicked(View view) {
        hideKeyboard(view);
        customPopupWindow.setClickable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() { codeViewShowAnimation(false, 100); }
        });
    }

    private void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); }
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

    private void codeViewShowAnimation(Boolean show, int duration) {
        AnimationSet animations = new AnimationSet(false);
        animations.setRepeatMode(0);

        if (show) {
            customPopup.setVisibility(View.VISIBLE);
            AlphaAnimation fadeInAni = new AlphaAnimation(0.0f, 1.0f);
            fadeInAni.setInterpolator(new AccelerateInterpolator());
            fadeInAni.setDuration(duration);
            animations.addAnimation(fadeInAni);

            Animation scaleInAni = new ScaleAnimation(0.97f, 1f, 0.97f, 1f, customPopupWindow.getWidth() / 2f, customPopupWindow.getHeight() / 2f);
            scaleInAni.setDuration(1000);
            scaleInAni.setInterpolator(new DecelerateInterpolator());
            customPopup.startAnimation(scaleInAni); }
        else {
            AlphaAnimation fadeOutAni = new AlphaAnimation(1.0f, 0.0f);
            fadeOutAni.setInterpolator(new AccelerateInterpolator());
            fadeOutAni.setDuration(duration);
            fadeOutAni.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    customPopup.setVisibility(View.INVISIBLE); }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            animations.addAnimation(fadeOutAni); }
        customPopup.startAnimation(animations);
    }
}

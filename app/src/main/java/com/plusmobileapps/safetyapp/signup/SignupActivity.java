package com.plusmobileapps.safetyapp.signup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.login.LoginActivity;
import com.plusmobileapps.safetyapp.main.MainActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class SignupActivity extends AppCompatActivity implements SignupContract.View, SignupDownloadCallback {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;
    private TextInputLayout nameInput, emailInput, passwordInput, passwordCheckInput, InputschoolNameInput;
    private TextView statusText, alertView;
    private Spinner schoolSpinner, roleSpinner;
    private CircularProgressButton signUpButton;
    private ArrayList<String> schoolList;
    private ArrayAdapter<String> schoolSpinnerList;
    private EditText newSchool, nameField, emailField, passwordField;
    private boolean schoolExists, userSignedUp = false;
    private SchoolDownloadFragment schoolDownloadFragment;
    boolean downloading;
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;
    private SignUpHandler signupCallback;
    String email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Debug"+TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, 2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        presenter = new SignupPresenter(this);

        schoolDownloadFragment = SchoolDownloadFragment.getInstance(getFragmentManager());
        schoolDownloadFragment.setCallback(this);

        schoolList = new ArrayList<>();
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
        alertView = findViewById(R.id.alertView);

        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(nameListener);
        Objects.requireNonNull(emailInput.getEditText()).addTextChangedListener(emailListener);
        Objects.requireNonNull(passwordInput.getEditText()).addTextChangedListener(passwordListener);
        Objects.requireNonNull(passwordCheckInput.getEditText()).addTextChangedListener(passwordCheckListener);

        statusText.setText("");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.signup_roles, R.layout.activity_signup_spinner);
        adapter.setDropDownViewResource(R.layout.activity_signup_spinner_dropdown);
        roleSpinner.setAdapter(adapter);

        userPool = new AwsServices().initAWSUserPool(this);
        initSignUpHandler();
        userAttributes = new CognitoUserAttributes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug"+TAG, "onResume");
        if (userSignedUp) launchLoginScreen();
        else {
            downloadSchools();
            presenter.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signUpButton.dispose();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void setPresenter(SignupContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void launchHomeScreen() {
        Intent signUp = new Intent(SignupActivity.this, MainActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(signUp, bundle);
        finish();
    }

    public void launchLoginScreen() {
        Intent signUp = new Intent(SignupActivity.this, LoginActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(signUp, bundle);
        finish();
    }

    @Override
    public void populateSchoolSpinner(ArrayList<String> schools) {
      /*  schoolList.remove("");
        schoolList.remove(getString(R.string.new_school));
        for (int i = 0; i < schools.size(); i++) {
            schoolList.add(schools.get(i));
            System.out.println(schoolList.get(i));
        if(schools.size()>0 ) {
            for (int i = 0; i < schools.size(); i++) {
                schoolList.add(schools.get(i));
                System.out.println(schoolList.get(i));
            }
        }
        schoolList.add("");
        schoolList.add(getString(R.string.new_school));
        Log.d(TAG, "Populating spinner with " + schoolList.size() + " schools");
        schoolSpinnerList = new ArrayAdapter<String>(this, R.layout.fragment_spinner_item, schoolList);
        schoolSpinnerList.setDropDownViewResource(R.layout.fragment_spinner_item);
        schoolSpinner.setAdapter(schoolSpinnerList);
        schoolSpinner.setSelection(0);
        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (getString(R.string.new_school).equals(schoolSpinner.getSelectedItem().toString())) {
                    newSchool.setVisibility(View.VISIBLE);
                    schoolSpinner.setVisibility(View.GONE);
                    schoolExists = false;
                } else {
                    schoolExists = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    */}


    private View.OnClickListener saveSignupClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            HashMap<String, String> formInput = new HashMap<>();
            String school;
            statusText.setText("");
            nameInput.clearFocus();
            emailInput.clearFocus();
            passwordInput.clearFocus();
            passwordCheckInput.clearFocus();

            if (schoolExists) {
                Spinner schoolInput = findViewById(R.id.spinner_signup_school_name);
                school = schoolInput.getSelectedItem().toString();
                Log.d(TAG, "Chosen School: " + school);
            } else {
                school = newSchool.getEditableText().toString();
                Log.d(TAG, "New School: " + school);
            }

            name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();
            email = Objects.requireNonNull(emailInput.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordInput.getEditText()).getText().toString();
            String passwordCheck = Objects.requireNonNull(passwordCheckInput.getEditText()).getText().toString();

            String role = roleSpinner.getSelectedItem().toString();

            formInput.put(SignupPresenter.NAME_INPUT, name);
            formInput.put(SignupPresenter.EMAIL_INPUT, email);
            formInput.put(SignupPresenter.PASSWORD_INPUT, password);
            formInput.put(SignupPresenter.PASSWORD_INPUT_CHECK, passwordCheck);
            formInput.put(SignupPresenter.SCHOOL_NAME_INPUT, school);
            formInput.put(SignupPresenter.ROLE_INPUT, role);

            Boolean inputReady = presenter.processFormInput(formInput);

            school = ""; // TODO delete when school is implemented
            if (inputReady) { // Call AWS
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
            presenter.nameTextAdded(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };

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

    private void downloadSchools() {
        if (!downloading && schoolDownloadFragment != null) {
            schoolDownloadFragment.getSchools();
            downloading = true; }
    }

    @Override
    public ArrayList<String> updateFromDownload(String result, ArrayList<String> schoolList) {
        downloading = true;
        Log.d(TAG, "Result from GetSchoolsTask: " + result);
        populateSchoolSpinner(schoolList);
        return schoolList;
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            // You can add UI behavior for progress updates here.
            case SignupDownloadCallback.Progress.ERROR:

                break;
            case SignupDownloadCallback.Progress.CONNECT_SUCCESS:

                break;
            case SignupDownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case SignupDownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case SignupDownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (schoolDownloadFragment != null) {
            schoolDownloadFragment.cancelGetSchools(); }
    }

    private void initSignUpHandler(){
        signupCallback = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                statusText.setText("");
                if (!signUpConfirmationState) { // User confirming via email code
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    userSignedUp = true;

                    // TODO Have button go to default user email app instead
                    builder.setCancelable(true);
                    builder.setTitle("Account Created");
                    builder.setMessage("Almost there! A confirmation email has been sent to "+email+ ".");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertView.setVisibility(View.VISIBLE);
                            launchLoginScreen();
                        }
                    });
                    builder.show(); }
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
            }
        };
    }

}

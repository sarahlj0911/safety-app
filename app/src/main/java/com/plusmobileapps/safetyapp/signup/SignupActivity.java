package com.plusmobileapps.safetyapp.signup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.amazonaws.regions.Regions.US_EAST_1;
import static com.amazonaws.regions.Regions.US_WEST_2;

public class SignupActivity extends AppCompatActivity implements SignupContract.View, SignupDownloadCallback {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;
    private TextInputLayout nameInput, emailInput, passwordInput, schoolNameInput;
    private TextView statusText;
    private Spinner schoolSpinner;
    private ArrayList<String> schoolList;
    private ArrayAdapter<String> schoolSpinnerList;
    private EditText newSchool, nameField, emailField;
    private boolean schoolExists;
    private SchoolDownloadFragment schoolDownloadFragment;
    boolean downloading;
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;
    SignUpHandler signupCallback;
    String email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        presenter = new SignupPresenter(this);
        Button saveSignupBtn = findViewById(R.id.button_save_signup);
        saveSignupBtn.setOnClickListener(saveSignupClickListener);

        schoolDownloadFragment = SchoolDownloadFragment.getInstance(getFragmentManager());
        schoolDownloadFragment.setCallback(this);

        schoolList = new ArrayList<>();
        schoolSpinner = findViewById(R.id.spinner_signup_school_name);
        newSchool = findViewById(R.id.new_school_text_box);

        nameInput = findViewById(R.id.signup_name);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);
        newSchool = findViewById(R.id.new_school_text_box);
        nameField = findViewById(R.id.fieldName);
        emailField = findViewById(R.id.fieldEmail);
        statusText = findViewById(R.id.textViewStatus);

        nameField.setNextFocusRightId(R.id.fieldEmail);
        emailField.setNextFocusRightId(R.id.fieldPassword);

        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(nameListener);
        Objects.requireNonNull(emailInput.getEditText()).addTextChangedListener(emailListener);

        statusText.setText(""); // Clear status

        AWSMobileClient.getInstance().initialize(getApplicationContext());

        initAWSUserPool();
        initSignUpHandler();
        userAttributes = new CognitoUserAttributes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadSchools();
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void setPresenter(SignupContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void launchHomeScreen() {
        PrefManager prefManager = new PrefManager(this);
        prefManager.setIsUserSignedUp(true);
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }


    public void launchConfirmationScreen(String email) {
        Intent intent = new Intent(SignupActivity.this, SignupConfirmActivity.class);
        intent.putExtra("userEmail", email);
        startActivity(intent);
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

            if (schoolExists) {
                Spinner schoolInput = findViewById(R.id.spinner_signup_school_name);
                school = schoolInput.getSelectedItem().toString();
                Log.d(TAG, "Chosen School: " + school);
            } else {
                school = newSchool.getEditableText().toString();
                Log.d(TAG, "New School: " + school);
            }

            Spinner roleInput = findViewById(R.id.spinner_signup_role);

            name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();
            email = Objects.requireNonNull(emailInput.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordInput.getEditText()).getText().toString();

            String role = roleInput.getSelectedItem().toString();

            formInput.put(SignupPresenter.NAME_INPUT, name);
            formInput.put(SignupPresenter.EMAIL_INPUT, email);
            formInput.put(SignupPresenter.PASSWORD_INPUT, password);
            formInput.put(SignupPresenter.SCHOOL_NAME_INPUT, school);
            formInput.put(SignupPresenter.ROLE_INPUT, role);

            Boolean inputReady = presenter.processFormInput(formInput);

            if (inputReady) { // Call AWS
                userAttributes.addAttribute("name", name);
                userAttributes.addAttribute("email", email);
                userAttributes.addAttribute("role", role);

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
    public void displayNoSchoolError(boolean show) {
        schoolNameInput.setError(getString(R.string.error_school_name));
        schoolNameInput.setErrorEnabled(show);
    }

    @Override
    public void displayNoPasswordError(boolean show) {
        passwordInput.setError(getString(R.string.error_school_name));
        passwordInput.setErrorEnabled(show);
    }

    @Override
    public void displayInvalidPasswordError(boolean show) {
        passwordInput.setError(getString(R.string.error_school_name));
        passwordInput.setErrorEnabled(show);
    }

    private TextWatcher nameListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.nameTextAdded();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher emailListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.emailTextAdded();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher schoolNameListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.schoolNameTextAdded();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void downloadSchools() {
        if (!downloading && schoolDownloadFragment != null) {
            schoolDownloadFragment.getSchools();
            downloading = true;
        }
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
            schoolDownloadFragment.cancelGetSchools();
        }
    }

    /**
     * Hides the keyboard
     * REF: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard#
     * Added by Jeremy Powell 1/24/2019
     */
    //
    public void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); }
    }

    private void initAWSUserPool(){
        String POOL_ID = "us-west-2_3KvLblMyN";
        String APP_ClIENT_ID = "1p97ciklq4r2fbapn15s0fignt";
        String APP_ClIENT_SECRET = "1ft9vtbauounq3vhlaelukluluc8qdru438iahuirqg5dscn56oh";
        Context CONTEXT = this;

        userPool = new CognitoUserPool(CONTEXT, POOL_ID, APP_ClIENT_ID, APP_ClIENT_SECRET, US_WEST_2);
    }

    private void initSignUpHandler(){
        signupCallback = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                statusText.setText("");
                if (!signUpConfirmationState) {
                    // User confirming via email code
                    launchConfirmationScreen(email); }
                else { launchHomeScreen(); }
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "User sign-up failed: " + exception.toString());
                String status = getApplicationContext().getString(R.string.status_text, name);
                statusText.setText(status);
            }
        };
    }

}

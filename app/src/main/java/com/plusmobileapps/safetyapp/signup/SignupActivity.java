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

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.login.LoginActivity;
import com.plusmobileapps.safetyapp.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements SignupContract.View, SignupDownloadCallback {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;
    private TextInputLayout nameInput;
    private TextInputLayout emailInput;
    private TextInputLayout schoolNameInput;
    private Spinner schoolSpinner;
    private ArrayList<String> schoolList;
    private ArrayAdapter<String> schoolSpinnerList;
    private EditText newSchool, emailField;
    private boolean schoolExists;
    private SchoolDownloadFragment schoolDownloadFragment;
    boolean downloading;

    // TODO add button function

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
        newSchool = findViewById(R.id.new_school_text_box);
        emailField = findViewById(R.id.fieldEmail);

        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(nameListener);
        Objects.requireNonNull(emailInput.getEditText()).addTextChangedListener(emailListener);

        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) hideKeyboard(v); }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadSchools();
        presenter.start();
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

            String name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();
            String email = Objects.requireNonNull(emailInput.getEditText()).getText().toString();

            String role = roleInput.getSelectedItem().toString();

            formInput.put(SignupPresenter.NAME_INPUT, name);
            formInput.put(SignupPresenter.EMAIL_INPUT, email);
            formInput.put(SignupPresenter.SCHOOL_NAME_INPUT, school);
            formInput.put(SignupPresenter.ROLE_INPUT, role);

            presenter.processFormInput(formInput);
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
     * Added by Jeremy Powell 1/24/2019
     */
    public void buttonGotoLogInClicked(View view) {
        android.util.Log.d(TAG, "Debug: Login Button Clicked");
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
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

}

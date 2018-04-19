package com.plusmobileapps.safetyapp.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.sip.SipSession;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.main.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;
    private TextInputLayout nameInput;
    private TextInputLayout emailInput;
    private TextInputLayout schoolNameInput;
    private Spinner schoolSpinner;
    private ArrayList<String> schoolList;
    private ArrayAdapter<String> schoolSpinnerList;
    private EditText newSchool;
    private boolean schoolExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        presenter = new SignupPresenter(this);
        Button saveSignupBtn = findViewById(R.id.button_save_signup);
        saveSignupBtn.setOnClickListener(saveSignupClickListener);
        schoolList = new ArrayList<String>();
        schoolSpinner = (Spinner)findViewById(R.id.spinner_signup_school_name);
        newSchool = (EditText)findViewById(R.id.new_school_text_box);

        nameInput = findViewById(R.id.signup_name);
        emailInput = findViewById(R.id.signup_email);
        newSchool = findViewById(R.id.new_school_text_box);

        nameInput.getEditText().addTextChangedListener(nameListener);
        emailInput.getEditText().addTextChangedListener(emailListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void populateSchoolSpinner(List<School> schools) {
        for(int i = 0; i < schools.size(); i++) {
            schoolList.add(schools.get(i).getSchoolName().toString());
        }
        schoolList.add(getString(R.string.new_school));
        Log.d(TAG, "Populating spinner with " + schools.size() + " schools");
        schoolSpinnerList = new ArrayAdapter<String>(this, R.layout.fragment_spinner_item, schoolList);
        schoolSpinnerList.setDropDownViewResource(R.layout.fragment_spinner_item);
        schoolSpinner.setAdapter(schoolSpinnerList);

        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(getString(R.string.new_school).equals(schoolSpinner.getSelectedItem().toString())) {
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
    }



    private View.OnClickListener saveSignupClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            HashMap<String, String> formInput = new HashMap<>();
            String school;

            if(schoolExists) {
                Spinner schoolInput = findViewById(R.id.spinner_signup_school_name);
                school = schoolInput.getSelectedItem().toString();
                Log.d(TAG, "Chosen School: " + school);
            } else {
                school = newSchool.getEditableText().toString();
                Log.d(TAG, "New School: " + school);
            }


            Spinner roleInput = findViewById(R.id.spinner_signup_role);

            String name = nameInput.getEditText().getText().toString();
            String email = emailInput.getEditText().getText().toString();

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

}

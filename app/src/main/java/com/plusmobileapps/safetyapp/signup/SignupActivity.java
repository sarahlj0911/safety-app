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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;
    private TextInputLayout nameInput;
    private TextInputLayout emailInput;
    private TextInputLayout schoolNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking if user has already signed up
        PrefManager prefManager = new PrefManager(this);
        prefManager.setFirstTimeLaunch(false);

        if (prefManager.isUserSignedUp()) {
            launchHomeScreen();
        }

        setContentView(R.layout.activity_signup);
        presenter = new SignupPresenter(this);
        Button saveSignupBtn = findViewById(R.id.button_save_signup);
        saveSignupBtn.setOnClickListener(saveSignupClickListener);

        nameInput = findViewById(R.id.signup_name);
        emailInput = findViewById(R.id.signup_email);
        schoolNameInput = findViewById(R.id.signup_school_name);

        nameInput.getEditText().addTextChangedListener(nameListener);
        emailInput.getEditText().addTextChangedListener(emailListener);
        schoolNameInput.getEditText().addTextChangedListener(schoolNameListener);
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
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }

    private View.OnClickListener saveSignupClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            HashMap<String, String> formInput = new HashMap<>();

            Spinner roleInput = findViewById(R.id.spinner_signup_role);

            String name = nameInput.getEditText().getText().toString();
            String email = emailInput.getEditText().getText().toString();
            String school = schoolNameInput.getEditText().getText().toString();
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

package com.plusmobileapps.safetyapp.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.main.MainActivity;

import java.util.HashMap;
import java.util.List;

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        presenter = new SignupPresenter(this);
        Button saveSignupBtn = findViewById(R.id.button_save_signup);
        saveSignupBtn.setOnClickListener(saveSignupClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Checking if user has already signed up
        PrefManager prefManager = new PrefManager(this);
        prefManager.setFirstTimeLaunch(false);

        if (prefManager.isUserSignedUp()) {
            launchHomeScreen();
            finish();
        }

        presenter.start();
    }

    @Override
    public void setPresenter(SignupContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }

    private View.OnClickListener saveSignupClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            HashMap<String, String> formInput = new HashMap<>();
            EditText nameInput = findViewById(R.id.signup_name);
            EditText emailInput = findViewById(R.id.signup_email);
            EditText schoolNameInput = findViewById(R.id.signup_school_name);
            Spinner roleInput = findViewById(R.id.spinner_signup_role);

            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String school = schoolNameInput.getText().toString();
            String role = roleInput.getSelectedItem().toString();

            formInput.put("Name", name);
            formInput.put("Email", email);
            formInput.put("School Name", school);
            formInput.put("Role", role);

            if (presenter.processFormInput(formInput)) {
                launchHomeScreen();
            }
        }
    };

    @Override
    public void displayErrors(List<String> errorMessages) {
        Log.d(TAG, "Errors to display...");

        for (String error : errorMessages) {
            Log.d(TAG, error);
        }
    }
}

package com.plusmobileapps.safetyapp.signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.plusmobileapps.safetyapp.R;

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    public static final String TAG = "SignupActivity";
    private SignupContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        presenter = new SignupPresenter(this);
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
}

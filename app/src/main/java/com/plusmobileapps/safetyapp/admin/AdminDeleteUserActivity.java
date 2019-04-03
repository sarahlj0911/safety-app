package com.plusmobileapps.safetyapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;

public class AdminDeleteUserActivity extends AppCompatActivity {
  TextView email;
    Button b1;
    CognitoUserPool userPool;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);
        userPool = new AwsServices().initAWSUserPool(this);
        //user id is the email

        //creates spinner to list users



    }
    public void backClicked(){
        Intent i=new Intent(AdminDeleteUserActivity.this,AdminMainActivity.class);
        startActivity(i);

    }
    public void onClickDelete(){
        email=(findViewById(R.id.editText3));
        userPool.getUser(email.getText().toString()).deleteUser(deleteUserCallback);
    }
    GenericHandler deleteUserCallback = new GenericHandler() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(Exception exception) {

        }
    };
}

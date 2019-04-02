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
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;

public class AdminDeleteUserActivity extends AppCompatActivity {
    String array[] = {"user1", "user2", "user3", "user4","user5"};
    Button b1;
    CognitoUserPool userPool;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);
        userPool = new AwsServices().initAWSUserPool(this);
        //user id is the email
        userPool.getUser("asasasas").deleteUser(deleteUserCallback);
        //creates spinner to list users
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.userArray,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), (CharSequence) parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void backClicked(){
        Intent i=new Intent(AdminDeleteUserActivity.this,AdminMainActivity.class);
        startActivity(i);

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

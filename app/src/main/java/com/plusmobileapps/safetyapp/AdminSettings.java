package com.plusmobileapps.safetyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.*;

public class AdminSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        final Button createuserbutton = findViewById(R.id.create_user_btn);
        final EditText username = findViewById(R.id.new_user_username);
        final EditText password = findViewById(R.id.new_user_password);
        final EditText schoolid= findViewById(R.id.new_user_school_id);
        final EditText userID = findViewById(R.id.new_user_userID);
        final EditText userTitle = findViewById(R.id.new_user_role);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        final Button createuserbutton = findViewById(R.id.create_user_btn);
        createuserbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //todo Imelement Shareef's create user functions

                //when done send notification to user via toast.
               Toast.makeText(getApplicationContext(),"User Added!",Toast.LENGTH_LONG);
            }

        });







    }


}

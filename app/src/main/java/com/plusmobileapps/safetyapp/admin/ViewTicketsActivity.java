package com.plusmobileapps.safetyapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;

public class ViewTicketsActivity extends AppCompatActivity {

        Button b1;

protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin_view_tickets);

        }
        public void backClicked(){
                Intent i=new Intent(ViewTicketsActivity.this,AdminMainActivity.class);
                startActivity(i);

        }

        }
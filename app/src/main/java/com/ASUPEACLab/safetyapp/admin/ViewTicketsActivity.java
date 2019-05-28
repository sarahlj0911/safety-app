package com.ASUPEACLab.safetyapp.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ASUPEACLab.safetyapp.R;
import com.ASUPEACLab.safetyapp.data.AppDatabase;

public class ViewTicketsActivity extends AppCompatActivity {

       AppDatabase ad;

protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin_view_tickets);


        }
        public void backClicked(View v){
        finish();

        }

        }
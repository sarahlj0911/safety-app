package com.plusmobileapps.safetyapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.plusmobileapps.safetyapp.AdminSettings;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingPresenter;

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
/*
* @author Shareef Levon
*
* Controller for adminmain screen which takes user to new activity based on the buttons clicked
* */
public class AdminMainActivity extends AppCompatActivity {

    Button b1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin_main);

    }
    public void viewTickets(View v){
        Log.d("onclick","viewtickets clicked");

        Intent i=new Intent(AdminMainActivity.this,ViewTicketsActivity.class);
        startActivity(i);
    }
    public void viewHistory(View v){
        Log.d("onclick","viewHistory clicked");
        Intent i=new Intent(AdminMainActivity.this,AdminViewHistoryActivity.class);
        startActivity(i);
    }
    public void addUser(View v){
        Log.d("onclick","addUser clicked ");

        Intent i=new Intent(AdminMainActivity.this, AdminSettings.class);
        startActivity(i);
    }
    public void deleteUser(View v){
        Log.d("onclick","viewtickets clicked");

        Intent i=new Intent(AdminMainActivity.this,AdminDeleteUserActivity.class);
        startActivity(i);
    }
    public void newTicket(View v){
        Log.d("onclick","viewtickets clicked");

    }
}
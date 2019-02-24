package com.plusmobileapps.safetyapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

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
    public void viewTickets(){
        Log.d("onclick","viewtickets");

        Intent i=new Intent(AdminMainActivity.this,ViewTicketsActivity.class);
        startActivity(i);
    }
    public void viewHistory(){
        Log.d("onclick","viewHistory");
        Intent i=new Intent(AdminMainActivity.this,AdminViewHistoryActivity.class);
        startActivity(i);
    }
    public void addUser(){
        Log.d("onclick","addUser");

        Intent i=new Intent(AdminMainActivity.this,AdminAddUserActivity.class);
        startActivity(i);
    }
    public void deleteUser(){
        Log.d("onclick","viewtickets");

        Intent i=new Intent(AdminMainActivity.this,AdminDeleteUserActivity.class);
        startActivity(i);
    }
    public void newTicket(){
        Log.d("onclick","viewtickets");

    }
}
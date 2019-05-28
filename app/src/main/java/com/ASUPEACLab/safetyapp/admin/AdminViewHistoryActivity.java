package com.ASUPEACLab.safetyapp.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ASUPEACLab.safetyapp.R;

public class AdminViewHistoryActivity extends AppCompatActivity {

    Button b1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin_view_history);

    }
    public void backClicked(View v){
        finish();

    }

}
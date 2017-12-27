package com.plusmobileapps.safetyapp.actionitems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.plusmobileapps.safetyapp.R;

public class ActionItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent callingIntent = getIntent();
        setContentView(R.layout.activity_action_item_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_item_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_action_item_detail);
    }
}

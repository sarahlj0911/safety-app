package com.plusmobileapps.safetyapp.actionitems;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.plusmobileapps.safetyapp.R;

public class ActionItemDetailActivity extends AppCompatActivity {
    Button editPriorityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent callingIntent = getIntent();
        setContentView(R.layout.activity_action_item_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_item_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_action_item_detail);

        editPriorityBtn = findViewById(R.id.edit_priority_btn);

    }

    public void showEditPriorityDialog(View view) {
        DialogFragment editPriorityDialog = new EditPriorityDialogFragment();
        editPriorityDialog.show(getSupportFragmentManager(), "EditPriorityDialogFragment");
    }
}

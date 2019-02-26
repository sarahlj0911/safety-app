package com.plusmobileapps.safetyapp.main;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.plusmobileapps.safetyapp.R;

public class removeUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        final Button button = findViewById(R.id.button2);

        final View view = findViewById(R.id.rootView);

        Toolbar myChildToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);





        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                Snackbar mySnackbar = Snackbar.make(view, R.string.user_not_found, Snackbar.LENGTH_SHORT);
                mySnackbar.show();
            }
        });
    }

}

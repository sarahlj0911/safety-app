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

import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;

public class AdminDeleteUserActivity extends AppCompatActivity {
<<<<<<< HEAD

=======
>>>>>>> 8938deb2e72c20083393b85b5cfdb1f7c354bb07
    String array[] = {"user1", "user2", "user3", "user4","user5"};
    Button b1;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);
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
}

package com.plusmobileapps.safetyapp.main;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.R;

import java.util.Map;

public class manageUser extends AppCompatActivity {

    private CognitoUserPool userPool;
    private AwsServices awsServices;
    private Context CONTEXT = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        final Button button = findViewById(R.id.button2);
        final EditText input = findViewById(R.id.editText);
        final View view = findViewById(R.id.rootView);

        Toolbar myChildToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        final Intent manage_user = new Intent(this, ManageSelectedUser.class);

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                awsServices = new AwsServices();
                initAWSUserPool();

                final String email = input.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        final CognitoUser user = userPool.getUser(email);


                        GetDetailsHandler handler = new GetDetailsHandler() {
                            @Override
                            public void onSuccess(final CognitoUserDetails list) {
                                Log.d("finduser", "Successfully retrieved user details");

                                Map userAtts    = list.getAttributes().getAttributes();
                                final String name = userAtts.get("name").toString();
                                final String role = userAtts.get("custom:role").toString();

                                Log.d("finduser", name + role);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Update UI.
                                        new AlertDialog.Builder(CONTEXT)
                                                .setMessage("Continue with this user: " + name + " with role: " + role + "?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        GenericHandler handler = new GenericHandler() {
                                                            @Override
                                                            public void onSuccess() {
                                                                Log.d("finduser","Delete was successful!");

                                                                startActivity(manage_user);
                                                            }

                                                            @Override
                                                            public void onFailure(Exception exception) {
                                                                Log.d("finduser","Delete failed, probe exception for details");
                                                            }
                                                        };
                                                        //to do
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Log.d("finduser", "confirmation dialog, no");
                                                    }
                                                })
                                                .show();
                                    }
                                });

                            }

                            @Override
                            public void onFailure(final Exception exception) {
                                Log.d("finduser", "Failed to retrieve the user details, probe exception for the cause/n"+exception);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Update UI.
                                        Snackbar mySnackbar = Snackbar.make(view, R.string.user_not_found, Snackbar.LENGTH_SHORT);
                                        mySnackbar.show();
                                    }
                                });
                            }
                        };
                        user.getDetails(handler);
                    }
                });

            }
        });
    }




    private void initAWSUserPool(){
        userPool = new CognitoUserPool(CONTEXT, awsServices.getPOOL_ID(), awsServices.getAPP_ClIENT_ID(), awsServices.getAPP_ClIENT_SECRET(), awsServices.getREGION());

    }

}

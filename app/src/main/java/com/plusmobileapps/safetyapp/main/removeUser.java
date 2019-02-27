package com.plusmobileapps.safetyapp.main;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.R;

public class removeUser extends AppCompatActivity {

    private CognitoUserPool userPool;
    private AwsServices awsServices;
    private Context CONTEXT = this;

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
                awsServices = new AwsServices();
                initAWSUserPool();

                CognitoUser user = userPool.getUser("bskyl@asu.edu");



                GetDetailsHandler handler = new GetDetailsHandler() {
                    @Override
                    public void onSuccess(final CognitoUserDetails list) {
                        Log.d("finduser", "Successfully retrieved user details");
                    }

                    @Override
                    public void onFailure(final Exception exception) {
                        Log.d("finduser", "Failed to retrieve the user details, probe exception for the cause");
                    }
                };
                user.getDetails(handler);

                /*GenericHandler handler = new GenericHandler() {

                    @Override
                    public void onSuccess() {
                        // Delete was successful!
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        // Delete failed, probe exception for details
                    }
                };
                user.deleteUser(handler);*/


                Snackbar mySnackbar = Snackbar.make(view, R.string.user_not_found, Snackbar.LENGTH_SHORT);
                mySnackbar.show();
            }
        });
    }

    private void initAWSUserPool(){
        userPool = new CognitoUserPool(CONTEXT, awsServices.getPOOL_ID(), awsServices.getAPP_ClIENT_ID(), awsServices.getAPP_ClIENT_SECRET(), awsServices.getREGION());
    }

}

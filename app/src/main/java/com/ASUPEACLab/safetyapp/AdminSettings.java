package com.ASUPEACLab.safetyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.ASUPEACLab.safetyapp.main.UserInfoDO;
import com.ASUPEACLab.safetyapp.R;

public class AdminSettings extends AppCompatActivity {

    DynamoDBMapper dynamoDBMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        final Button createuserbutton = findViewById(R.id.create_user_btn);
        final EditText username = findViewById(R.id.new_user_username);
        final EditText password = findViewById(R.id.new_user_password);
        final EditText schoolid= findViewById(R.id.new_user_school_id);
        final EditText userID = findViewById(R.id.new_user_userID);
        final EditText userTitle = findViewById(R.id.new_user_role);



        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        Log.d("AdminSettingsActivity", "Ready");


        createuserbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //add to dynamodb
                final UserInfoDO item = new UserInfoDO();
                item.setUserId(userID.getText().toString());
                item.setName(username.getText().toString());
                item.setTempPassword(password.getText().toString());
                item.setTitle(userTitle.getText().toString());
                item.setLanguage("eng");
                item.setLocation(schoolid.getText().toString());
                Log.d("AWS", "createUserInfoItem");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dynamoDBMapper.save(item);
                        // Item saved
                        Log.d("AWS", "item added");
                    }
                }).start();

                //when done send notification to user via toast.


               Toast.makeText(getApplicationContext(),"User Added!",Toast.LENGTH_LONG);

            }

        });







    }


}

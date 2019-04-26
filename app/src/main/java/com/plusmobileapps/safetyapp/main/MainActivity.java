package com.plusmobileapps.safetyapp.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;

import com.plusmobileapps.safetyapp.admin.AdminDeleteUserActivity;
import com.plusmobileapps.safetyapp.admin.AdminMainActivity;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.User;
import com.plusmobileapps.safetyapp.login.LoginActivity;
import com.plusmobileapps.safetyapp.signup.SaveSchoolTask;
import com.plusmobileapps.safetyapp.signup.SaveUserTask;
import com.plusmobileapps.safetyapp.summary.landing.SummaryPresenter;
import com.plusmobileapps.safetyapp.util.ActionItemsExport;
import com.plusmobileapps.safetyapp.util.FileUtil;
import com.plusmobileapps.safetyapp.util.exportPdf;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingPresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import id.zelory.compressor.Compressor;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = "MainActivity";
    private static final String AWSTAG = "MainActivityAWS";
    private TextView mTextMessage;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private String walkthroughFragmentTitle = "";
    private MainActivityPresenter presenter;
    private String selectedSchool = "";
    private Menu menu;

    // AWS
    private CognitoUserPool userPool;
    private CognitoUser user;
    private String userEmail, userName, userRole, userSchool;

    // SyncAdapter Constants
    public static final String AUTHORITY = "com.plusmobileapps.safetyapp.fileprovider"; // The authority for the sync adapter's content provider
    public static final String ACCOUNT_TYPE = "safetyapp.com";                          // An account type, in the form of a domain name
    public static final String ACCOUNT = "safetyapp";                                   // The account name

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    // Instance fields
    Account account;
    ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        userRole="default";
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.view_pager);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setAppBarTitle(0);

        MainActivityFragmentFactory factory = new MainActivityFragmentFactory();
        setUpPresenters(factory);
        presenter = new MainActivityPresenter(this);

        userPool = new AwsServices().initAWSUserPool(this);

        //Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (Objects.equals(extras.getString("activity"), "from login")) {
            userName = extras.getString("name");
            userRole = extras.getString("role");
            userSchool = extras.getString("school");
            userEmail = extras.getString("email");
            selectedSchool = userSchool;
            user = userPool.getUser(userEmail);
        }
        else {
            userEmail = extras.getString("email");
            user = userPool.getUser(userEmail);
            user.getDetailsInBackground(getUserDetailsHandler);
            selectedSchool = "newSchool";
        }


        //download pictures from aws
        Log.d(TAG,"Getting user info from Login activity "+userName+", "+userName+", "+userRole+", "+userSchool);
        List<Response> actionItems = new ArrayList<>(0);
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        actionItems = responseDao.getAllActionItems(1);
        for (int i = 0; i < actionItems.size(); i++) {
            Response actionItem = actionItems.get(i);
            if(actionItem.getImagePath()!=null) {
                String[] picdest = actionItem.getImagePath().split("/");
                String filename = picdest[picdest.length - 1];
                FileUtil.download(this, selectedSchool + "/ActionItemPictures/" + filename, actionItem.getImagePath());
            }
        }


        //put info into the db
           School school = new School(1, userSchool);
           AsyncTask<Void, Void, Boolean> saveSchoolTask = new SaveSchoolTask(school).execute();
           try {
               saveSchoolTask.get();
           } catch (InterruptedException | ExecutionException e) {
               Log.d(TAG, "Problem saving school");
               Log.d(TAG, e.getMessage());
           }
           User userToEnter = new User(1, 1, userEmail, userName, userRole);

           AsyncTask<Void, Void, Boolean> saveUserTask = new SaveUserTask(userToEnter).execute();
           try {
               saveUserTask.get();
           } catch (InterruptedException | ExecutionException e) {
               Log.d(TAG, "Issue saving user");
               Log.d(TAG, e.getMessage());

           }

        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this, awsStartupResult -> Log.d(AWSTAG, "You are connected to the AWS database!")).execute();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final MainSwipeAdapter swipeAdapter = new MainSwipeAdapter(getSupportFragmentManager(), factory);
        viewPager.setAdapter(swipeAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setOffscreenPageLimit(3);

        // Turn on periodic syncing
        contentResolver = getContentResolver();
        account = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);

        userPool = new AwsServices().initAWSUserPool(this);
        user = userPool.getUser(userEmail);
        user.getDetailsInBackground(getUserDetailsHandler);

    }


    @Override
    public void onResume() {
        super.onResume();
        user.getSession(authenticationHandler);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        try {
            List<Response> actionItems = new ArrayList<>(0);
            AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            ResponseDao responseDao = db.responseDao();
            actionItems = responseDao.getAllActionItems(1);
            for (int i = 0; i < actionItems.size(); i++) {
                Response actionItem = actionItems.get(i);
                if(actionItem.getImagePath()!=null) {
                    String[] picdest = actionItem.getImagePath().split("/");
                    String filename = picdest[picdest.length - 1];
                    File uncompressedpic = new File(actionItem.getImagePath());
                    File compressedpic = new Compressor(this).compressToFile(uncompressedpic);
                    //replaces picture
                    copyFile(compressedpic,uncompressedpic);
                    FileUtil.upload(this, selectedSchool + "/ActionItemPictures/" + filename, actionItem.getImagePath());
                }
            }
            FileUtil.upload(this, selectedSchool+"/appDB.db", "/data/data/com.plusmobileapps.safetyapp/databases/appDB.db");
            FileUtil.upload(this, selectedSchool+"/appDB.db-shm", "/data/data/com.plusmobileapps.safetyapp/databases/appDB.db-shm");
            FileUtil.upload(this, selectedSchool+"/appDB.db-wal", "/data/data/com.plusmobileapps.safetyapp/databases/appDB.db-wal");
        }
        catch(Exception ex) {ex.printStackTrace();}
        user.signOut();
        Log.d(AWSTAG, "Signed out user "+userEmail+" automatically");
    }

    @Override
    public void changePage(int position) {
        viewPager.setCurrentItem(position, true);
        setAppBarTitle(position);
    }

    @Override
    public void changeNavHighlight(int position){
        navigation.setSelectedItemId(position);
        setAppBarTitle(position);
    }

    private void launchLoginScreen() {
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out_none);
        finish();
    }


    private void setUpPresenters(MainActivityFragmentFactory factory) {
        new WalkthroughLandingPresenter(factory.getWalkthroughLandingFragment());
        new ActionItemPresenter(factory.getActionItemsFragment());
        new SummaryPresenter(factory.getSummaryFragment());
    }

    private void setAppBarTitle(int index) {
        switch (index) {
            case 0:
                setToolbarTitle(getString(R.string.title_walkthrough));
                break;
            case 1:
                setToolbarTitle(getString(R.string.title_action_items));
                break;
            case 2:
                setToolbarTitle(getString(R.string.title_summary));
                break;
            default:
                break;
        }
    }

    private void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Handle clicks of the bottom navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_walkthrough:
                    presenter.navButtonClicked(0);
                    return true;
                case R.id.navigation_dashboard:
                    presenter.navButtonClicked(1);
                    return true;
                //case R.id.navigation_history:
                //    presenter.navButtonClicked(2);
                //    return true;
            }
            return false;
        }

    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handle ViewPager page change events
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            presenter.pageSwipedTo(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    /**
     * Create a new dummy account for the sync adapter
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            Log.d(TAG, "Sync account created!");
            /*
             * If you don't set android:syncable="true" in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1) here.
             */
        }
        else Log.d(TAG, "Account already exists or some other error occurred.");
        return newAccount;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_menu,menu);

       this.menu=menu;

        Log.d("ROLE!!!!!",userRole);
      /*  if(userRole.compareToIgnoreCase("Administrator")!=0){
            MenuItem adminSettings = menu.findItem(R.id.settings_menu_admin);
            Log.d("ROLE!!!!!",userRole);
            adminSettings.setVisible(false);


        }*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.settings_menu_signout:
                // Settings selected
                signOutUser();
                launchLoginScreen();
                break;

          case R.id.settings_menu_admin:
                // Settings selected
              //todo change AdminDeleteUserActivity to AdminMainActivity
              //todo finish implmentation of AdminAddUserActivity, AdminNewTicketActivity, and AdminViewHistoryActivity
                    Intent admin = new Intent(this, AdminDeleteUserActivity.class);

                    startActivity(admin);
                break;

          case R.id.ExportActionPlan:
                    ActionItemsExport html = new ActionItemsExport();
                    html.exportActionItems();
                    Intent printIntent = new Intent(this, exportPdf.class);
                    startActivity(printIntent);
                    break;
        }
        return super.onOptionsItemSelected(item);
    }


    GetDetailsHandler getUserDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(final CognitoUserDetails list) {
            // Successfully retrieved user details
            userName = list.getAttributes().getAttributes().get("name");
            userRole = list.getAttributes().getAttributes().get("custom:role");
            userSchool = list.getAttributes().getAttributes().get("custom:school");

          // If the userRole is not an administrator, take away the admin settings menu item
            if(!userRole.equals("Administrator")){
                MenuItem adminSettings = menu.findItem(R.id.settings_menu_admin);
                adminSettings.setVisible(false);
            }
          
            userEmail = list.getAttributes().getAttributes().get("email");
            Log.d(AWSTAG, "Successfully loaded " + userName + " as role " + userRole + " at school " + userSchool);
        }
        @Override
        public void onFailure(final Exception exception) {
            Log.d(AWSTAG, "Failed to retrieve the user's details: " + exception);
        }

    };

        public void signOutUser() {
            try {
                user.signOut();
                Log.d(AWSTAG, "Signed out user " + userEmail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.d(AWSTAG, "User " + userEmail + " automatically signed back in");
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                Log.d(AWSTAG, "Refreshing user " + userEmail + " details");
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                Log.d(AWSTAG, "Encountered MFA challenge. Sending user back to login...");
                launchLoginScreen();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.d(AWSTAG, "Encountered authentication challenge. Sending user back to login...");
                launchLoginScreen();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(AWSTAG, "Unable to login user: " + exception);
            }
        };
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        if (destFile.exists()) {
            destFile.delete();
        }
        FileChannel source = null;
        FileChannel destination = null;

        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        destination.transferFrom(source, 0, source.size());
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    }

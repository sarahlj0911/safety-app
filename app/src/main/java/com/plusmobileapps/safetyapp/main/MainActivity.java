package com.plusmobileapps.safetyapp.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.plusmobileapps.safetyapp.AdminSettings;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.plusmobileapps.safetyapp.AwsServices;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.summary.landing.SummaryPresenter;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingPresenter;



import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = "MainActivity";
    private static final String AWSTAG = "AWS";
    private TextView mTextMessage;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private String walkthroughFragmentTitle = "";
    private MainActivityPresenter presenter;

    // AWS
    private CognitoUserPool userPool;
    private CognitoUser user;
    private String userEmail, userRole, userSchool;

    // DB mapper
    DynamoDBMapper dynamoDBMapper;

    // SyncAdapter Constants
    public static final String AUTHORITY = "com.plusmobileapps.safetyapp.provider";     // The authority for the sync adapter's content provider
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
        findViewsById();
        MainActivityFragmentFactory factory = new MainActivityFragmentFactory();
        setUpPresenters(factory);
        presenter = new MainActivityPresenter(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final MainSwipeAdapter swipeAdapter = new MainSwipeAdapter(getSupportFragmentManager(), factory);
        viewPager.setAdapter(swipeAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setOffscreenPageLimit(3);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setAppBarTitle(0);

        // Turn on periodic syncing
        contentResolver = getContentResolver();
        account = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);

        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d(AWSTAG, "You are connected to AWS!");
            }
        }).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        createUserInfoItem();

        userPool = new AwsServices().initAWSUserPool(this);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkForActiveToken()) {
            // Get user from from login
            user = userPool.getUser(userEmail);
            // TODO Sign user back in
            user.getDetailsInBackground(getUserDetailsHandler);
        }
        else {
            // TODO Prompt with "Session has timed out" -> Send to login page
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        user.signOut();
        Log.d(AWSTAG, "User " +userEmail+ " has been signed out");
    }


    private void setUpPresenters(MainActivityFragmentFactory factory) {
        new WalkthroughLandingPresenter(factory.getWalkthroughLandingFragment());
        new ActionItemPresenter(factory.getActionItemsFragment());
        new SummaryPresenter(factory.getSummaryFragment());
    }

    private void findViewsById() {
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.view_pager);
    }

    @Override
    public void changePage(int position) {
        viewPager.setCurrentItem(position, true);
        setAppBarTitle(position);
    }

    @Override
    public void changeNavHighlight(int position) {
        navigation.setSelectedItemId(position);
        setAppBarTitle(position);
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
     *
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
            /*
             * If you don't set android:syncable="true" in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1) here.
             */
        } else {
            Log.d(TAG, "Account already exists or some other error occurred.");
        }

        Log.d(TAG, "Sync account created!");

        return newAccount;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.settings_menu:
                // Settings selected
                Intent adminSettings = new Intent(this, AdminSettings.class);
                startActivity(adminSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createUserInfoItem() {
        //an example to demonstrate a dynamoDB push to amazon web servers
        final UserInfoDO item = new UserInfoDO();
        item.setUserId("bart-test");
        item.setName("bart");
        item.setTitle("student");
        item.setLanguage("eng");
        item.setLocation("asu");
        Log.d(AWSTAG, "createUserInfoItem");

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(item);
                // Item saved
                Log.d(AWSTAG, "item added");
            }
        }).start();
    }

    private boolean checkForActiveToken(){
        // TODO check if token is still valid
        return true;
    }

    GetDetailsHandler getUserDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(final CognitoUserDetails list) {
            // Successfully retrieved user details
            userRole = list.getAttributes().getAttributes().get("custom:role");
            userSchool = list.getAttributes().getAttributes().get("custom:school");
            Log.d(AWSTAG, "Successfully loaded " + userEmail+ " as role " +userRole+ " at school " + userSchool);
        }

        @Override
        public void onFailure(final Exception exception) {
            Log.d(AWSTAG, "Failed to retrieve the user's details: " + exception);
        }
    };

}

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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
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
import com.plusmobileapps.safetyapp.util.FileUtil;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingPresenter;



import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;

    private AwsServices awsServices;
    private CognitoUserPool userPool;
    private Context CONTEXT = this;
    private CognitoUser user;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private String walkthroughFragmentTitle = "";
    private MainActivityPresenter presenter;

    String selectedSchool = "";

    //db mapper
    DynamoDBMapper dynamoDBMapper;

    // SyncAdapter Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.plusmobileapps.safetyapp.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "safetyapp.com";
    // The account name
    public static final String ACCOUNT = "safetyapp";
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
        awsServices = new AwsServices();
        userPool = new CognitoUserPool(CONTEXT, awsServices.getPOOL_ID(), awsServices.getAPP_ClIENT_ID(), awsServices.getAPP_ClIENT_SECRET(), awsServices.getREGION());
        user = userPool.getUser("shadow13524@gmail.com");

        selectedSchool = "NathanTestDBfiles";
        FileUtil.download(this, selectedSchool +"/appDB.db", "/data/data/com.plusmobileapps.safetyapp/databases/appDB.db");
        FileUtil.download(this, selectedSchool +"/appDB.db-shm", "/data/data/com.plusmobileapps.safetyapp/databases/appDB.db-shm");
        FileUtil.download(this, selectedSchool +"/appDB.db-wal", "/data/data/com.plusmobileapps.safetyapp/databases/appDB.db-wal");
        //FileUtil.upload(this, selectedSchool +"/awss3transfertable.db", "/data/data/com.plusmobileapps.safetyapp/databases/awss3transfertable.db");
        //FileUtil.upload(this, selectedSchool +"/awss3transfertable.db-shm", "/data/data/com.plusmobileapps.safetyapp/databases/awss3transfertable.db-shm");
        //FileUtil.upload(this, selectedSchool +"/awss3transfertable.db-wal", "/data/data/com.plusmobileapps.safetyapp/databases/awss3transfertable.db-wal");


        setUpPresenters(factory);


        //boolean fileDeleted = FileUtil.deleteDb(this);

        //FileUtil.download(this, "uploads/appDB1.db", "/data/data/com.plusmobileapps.safetyapp/databases/");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                switch (userStateDetails.getUserState()) {
                    case SIGNED_IN:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("YourMainActivity", "User has signed in");
                            }
                        });
                        break;
                    case SIGNED_OUT:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO show user not signed in prompt
                                Log.d("YourMainActivity", "User not signed in");
                            }
                        });
                        break;
                    case SIGNED_OUT_USER_POOLS_TOKENS_INVALID:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO show user inactive signed out prompt
                                Log.d("YourMainActivity", "User has been signed out");
                            }
                        });
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("INIT", e.toString());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("YourMainActivity", "User has been signed out");
        user.signOut();
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
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            presenter.pageSwipedTo(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
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
                //settings selected
                Intent adminsettings = new Intent(this, AdminSettings.class);
                startActivity(adminsettings);
                break;

            case R.id.settings_menu_remove:
                //settings selected
                Intent remove_user = new Intent(this, removeUser.class);
                startActivity(remove_user);
                break;
        }


        return super.onOptionsItemSelected(item);
    }


}

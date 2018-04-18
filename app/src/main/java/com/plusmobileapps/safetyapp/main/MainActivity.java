package com.plusmobileapps.safetyapp.main;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.summary.landing.SummaryPresenter;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private String walkthroughFragmentTitle = "";
    private MainActivityPresenter presenter;

    // TODO Uncomment these to try the periodic sync
    // SyncAdapter Constants
    // The authority for the sync adapter's content provier
    //public static final String AUTHORITY = "com.plusmobileapps.safetyapp.provider";
    // An account type, in the form of a domain name
    //public static final String ACCOUNT_TYPE = "safetyapp.com";
    // The account name
    //public static final String ACCOUNT = "default_account";
    // Instance fields
    //Account account;
    //ContentResolver mResolver;

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

        // TODO Uncomment this to attempt periodic syncing. Haven't gotten it to work yet, so we'll see
        // Turn on periodic syncing
        /* ContentResolver.addPeriodicSync(
                account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL
        );*/
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
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
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
}

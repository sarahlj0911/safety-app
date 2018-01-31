package com.plusmobileapps.safetyapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingAdapter;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingFragment;
import com.plusmobileapps.safetyapp.surveys.location.LocationFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private LocationFragment locationFragment;
    private SurveyLandingFragment surveyLandingFragment;
    private String surveyFragmentTitle = getString(R.string.walk_throughs);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_survey:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1, true);
                    return true;
                case R.id.navigation_history:
                    viewPager.setCurrentItem(2, true);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        final MainSwipeAdapter swipeAdapter = new MainSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setAppBarTitle(surveyFragmentTitle);
                        navigation.setSelectedItemId(R.id.navigation_survey);
                        break;
                    case 1:
                        setAppBarTitle("Action Items");
                        navigation.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case 2:
                        setAppBarTitle("Summary");
                        navigation.setSelectedItemId(R.id.navigation_history);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setAppBarTitle("Surveys");
    }

    private void setAppBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        surveyFragmentTitle = getString(R.string.walk_throughs);
        setAppBarTitle(surveyFragmentTitle);
    }
}

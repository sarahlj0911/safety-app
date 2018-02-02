package com.plusmobileapps.safetyapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemsFragment;
import com.plusmobileapps.safetyapp.summary.landing.SummaryFragment;
import com.plusmobileapps.safetyapp.summary.landing.SummaryPresenter;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingFragment;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingPresenter;
import com.plusmobileapps.safetyapp.surveys.location.LocationFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private String surveyFragmentTitle = "";
    private SurveyLandingPresenter surveyLandingPresenter;
    private ActionItemPresenter actionItemPresenter;
    private SummaryPresenter summaryPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
        setUpPresenters();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final MainSwipeAdapter swipeAdapter = new MainSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setAppBarTitle(getString(R.string.walk_throughs));
    }

    private void setUpPresenters() {
        surveyLandingPresenter = new SurveyLandingPresenter(FragmentFactory.getInstance().getSurveyLandingFragment());
        actionItemPresenter = new ActionItemPresenter(FragmentFactory.getInstance().getActionItemsFragment());
        summaryPresenter = new SummaryPresenter(FragmentFactory.getInstance().getSummaryFragment());
    }

    private void findViewsById() {
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.view_pager);
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

    /**
     * Handle clicks of the bottom navigation
     */
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


    /**
     * Handle ViewPager page change events
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
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
    };
}

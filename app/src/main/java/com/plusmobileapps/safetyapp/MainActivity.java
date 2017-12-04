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

public class MainActivity extends AppCompatActivity
        implements SurveyLandingAdapter.CardViewHolder.OnSurveySelectedListener{

    private TextView mTextMessage;

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private LocationFragment locationFragment;
    private SurveyLandingFragment surveyLandingFragment;
    private String surveyFragmentTitle = "Surveys";

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


    @Override
    public void onSurveySelected(int position) {
        surveyLandingFragment = (SurveyLandingFragment) getSupportFragmentManager().findFragmentById(R.id.root_frame);
        surveyFragmentTitle = surveyLandingFragment.getSurveyTitle(position);
        setAppBarTitle(surveyFragmentTitle);
        if (position == 1) {
            locationFragment = LocationFragment.newInstance(false);
        } else {
            locationFragment = LocationFragment.newInstance(true);
        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.root_frame, locationFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("survey")
                .commit();

    }

    public void createSurveyButtonClicked(View view) {
        surveyLandingFragment = ((SurveyLandingFragment) getSupportFragmentManager().findFragmentById(R.id.root_frame));

        if (surveyLandingFragment.isSurveyInProgress()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You have an unfinished survey. Creating a new survey will delete the unfinished one. Are you sure you want to create a new survey?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createNewSurvey();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            createNewSurvey();
        }
    }

    private void createNewSurvey() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Survey")
                .setView(getLayoutInflater().inflate(R.layout.dialog_create_survey, null))
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog dialogObj = Dialog.class.cast(dialog);
                        EditText editText = dialogObj.findViewById(R.id.edittext_create_survey);
                        surveyFragmentTitle = editText.getText().toString();
                        setAppBarTitle(surveyFragmentTitle);
                        locationFragment = LocationFragment.newInstance();

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.root_frame, locationFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack("survey")
                                .commit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user clicked cancel
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setAppBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        surveyFragmentTitle = "Surveys";
        setAppBarTitle(surveyFragmentTitle);
    }
}

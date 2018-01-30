package com.plusmobileapps.safetyapp.surveys.survey;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.plusmobileapps.safetyapp.R;

import java.util.ArrayList;

public class WalkthroughActivity extends AppCompatActivity {

    static final String TAG = "WalkthroughActivity";
    public static final String EXTRA_LOCATION = "com.plusmobileapps.safetyapp.walkthroughQuestion.overview.LOCATION";
    FragmentManager fragmentManager;
    WalkthroughQuestion walkthroughQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String location = intent.getExtras().getString(EXTRA_LOCATION);
        setContentView(R.layout.activity_walkthrough);
        Toolbar toolbar = (Toolbar) findViewById(R.id.walkthrough_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(location);
        fragmentManager = getFragmentManager();

        populateWalkthroughQuestion(location);
        WalkthroughContentFragment fragment = WalkthroughContentFragment.newInstance(walkthroughQuestion);
        FragmentTransaction initialTransaction = fragmentManager.beginTransaction();
        initialTransaction
                .add(R.id.suvey_container, fragment, "0")
                .commit();
    }

    /**
     * handle the click events of the radio button group
     *
     * @param view  radiobutton from the walkthroughQuestion
     */
    public void onRadioButtonClicked(View view) {
        walkthroughQuestion.setRating(((RadioButton) view).getText().toString());

        Log.d(TAG, "New rating: " + walkthroughQuestion.getRating());
    }

    /**
     * handle click event of the back button
     *
     * @param view  back button
     */
    public void onBackButtonPress(View view) {
        final int count = fragmentManager.getBackStackEntryCount();

        if (count < 1) {
            finish();   //finish whole activity if nothing on backstack
        } else {
            fragmentManager.popBackStack();     //move back one on fragment transaction backstack
        }
    }

    /**
     * handle click event of next button
     *
     * @param view  next button
     */
    public void onNextButtonPressed(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String currentFragmentTag = Integer.toString(fragmentManager.getBackStackEntryCount());
        //int id = fragmentManager.findFragmentByTag(currentFragmentTag);

        WalkthroughContentFragment newInstance = WalkthroughContentFragment.newInstance(walkthroughQuestion);

        transaction
                .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_out_left,
                        R.animator.slide_in_right,
                        R.animator.slide_out_right)
                .replace(R.id.suvey_container, newInstance)
                .addToBackStack("walkthroughQuestion")
                .commit();
    }

    private void populateWalkthroughQuestion(String location) {
        ArrayList<String> options = new ArrayList<>();
        options.add("None");
        options.add("1-2");
        options.add("3-5");
        options.add("6-10");
        walkthroughQuestion = new WalkthroughQuestion(
                location,
                "This is what you should be looking for in the boys bathroom while you are walking through the bathroom",
                options,
                null);
    }
}

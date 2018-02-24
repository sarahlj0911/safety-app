package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//Look at SummaryOverviewDetailsActivity
public class WalkthroughActivity extends AppCompatActivity  {

    static final String TAG = "WalkthroughActivity";
    public static final String EXTRA_LOCATION = "com.plusmobileapps.safetyapp.walkthrough.overview.LOCATION";
    FragmentManager fragmentManager;
    Question walkthroughQuestion;
    int currentPosition;
    WalkthroughPresenter presenter;
    List<Question> questions;
    WalkthroughActivityModel model;
    private AsyncTask<Void, Void, List<Question>> loadQuestions;

    //Use array list of fragments.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int locationId = intent.getExtras().getInt(EXTRA_LOCATION);
        model = new WalkthroughActivityModel(locationId);
        loadQuestions = model.execute();

        try {
            this.questions = model.get();
        } catch (InterruptedException | ExecutionException e)  {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_walkthrough);
        Toolbar toolbar = (Toolbar) findViewById(R.id.walkthrough_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(location);
        fragmentManager = getFragmentManager();

        populateWalkthroughQuestion(location);
        WalkthroughContentFragment fragment = WalkthroughContentFragment.newInstance(walkthroughQuestion);

        FragmentTransaction initialTransaction = fragmentManager.beginTransaction();

        //CREATE ALL FRAGMENTS in list/array/hashmap.

        //launch the first fragment.
        initialTransaction
                .add(R.id.walkthrough_container, fragment, "0")
                .commit();
    }

    /**
     * handle the click events of the radio button group
     *
     * @param view  radiobutton from the walkthroughQuestion
     */
    public void onRadioButtonClicked(View view) {
        String rating = ((RadioButton) view).getText().toString();

        walkthroughQuestion.setRating(rating);

        Log.d(TAG, "New rating: " + walkthroughQuestion.getRating());
    }

    /**
     * handle click event of the back button
     *
     * @param view  back button
     */
    public void onBackButtonPress(View view) {
        //decrement position
        //launch framgment.

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

        //increpement position
        //launch fragement

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
                .replace(R.id.walkthrough_container, newInstance)
                .addToBackStack("walkthroughQuestion")
                .commit();
    }

    private void populateWalkthroughQuestion(String location) {
        walkthroughQuestion = new Question(1,"This is fake", "this is fake", "None",
                "1-2", "3-5", "6-10");
    }
}

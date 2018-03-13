package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.walkthrough.location.LocationActivity;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentFragment;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentPresenter;

import java.util.List;

//Look at SummaryOverviewDetailsActivity
public class WalkthroughActivity extends AppCompatActivity implements WalkthroughContract.View {

    static final String TAG = "WalkthroughActivity";
    public static final String EXTRA_LOCATION_ID = "com.plusmobileapps.safetyapp.walkthrough.overview.LOCATION";
    FragmentManager fragmentManager;
    WalkthroughPresenter presenter = new WalkthroughPresenter(this);;
    private int locationId;
    private int walkthroughId;
    private WalkthroughContentPresenter currentContentPresenter;

    //Use array list of fragments.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        locationId = intent.getExtras().getInt(EXTRA_LOCATION_ID);
        walkthroughId = intent.getExtras().getInt(LocationActivity.EXTRA_WALKTHROUGH_ID);

        String locationName = intent.getExtras().getString(LocationActivity.EXTRA_WALKTHROUGH_LOCATION_NAME);
        setContentView(R.layout.activity_walkthrough);
        Toolbar toolbar = (Toolbar) findViewById(R.id.walkthrough_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(locationName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //will make the back button appear on toolbar
        fragmentManager = getSupportFragmentManager();

        Button previousButton = findViewById(R.id.previous_question);
        previousButton.setOnClickListener(previousButtonListener);
        Button nextButton = findViewById(R.id.next_question);
        nextButton.setOnClickListener(nextButtonListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start(locationId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.backButtonPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit_confirmation_save_message)
                .setTitle(R.string.exit_confirmation_save)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.confirmationExitClicked();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showNextQuestion(Question question) {
        WalkthroughContentFragment fragment = WalkthroughContentFragment.newInstance(question);
        currentContentPresenter = new WalkthroughContentPresenter(fragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction
                .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_out_left,
                        R.animator.slide_in_right,
                        R.animator.slide_out_right)
                .replace(R.id.walkthrough_container, fragment)
                .addToBackStack("walkthroughQuestion")
                .commit();
    }

    @Override
    public void showPreviousQuestion() {
        fragmentManager.popBackStack();
    }

    @Override
    public Response getCurrentResponse() {
        return currentContentPresenter.getResponse();
    }

    @Override
    public void closeWalkthrough() {
        finish();
    }

    @Override
    public void onBackPressed() {
        presenter.backButtonPressed();
    }

    View.OnClickListener previousButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.previousQuestionClicked();
        }
    };

    View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.nextQuestionClicked();
        }
    };
}

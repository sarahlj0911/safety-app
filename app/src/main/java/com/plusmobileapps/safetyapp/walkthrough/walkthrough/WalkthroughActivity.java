package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.walkthrough.location.LocationActivity;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentFragment;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentPresenter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class WalkthroughActivity extends AppCompatActivity implements WalkthroughContract.View {

    static final String TAG = "WalkthroughActivity";
    public static final String EXTRA_LOCATION_ID = "com.plusmobileapps.safetyapp.walkthrough.overview.LOCATION";
    FragmentManager fragmentManager;
    WalkthroughPresenter presenter = new WalkthroughPresenter(this);

    private int locationId;
    private int walkthroughId;
    private WalkthroughContentPresenter currentContentPresenter;
    private WalkthroughContentPresenter previousContentPresenter;
    private WalkthroughContentFragment fragment;
    private Stack<WalkthroughContentPresenter> presenterStack = new Stack<>();

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
    protected void onDestroy() {
        super.onDestroy();
        presenter.saveQuestions();
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
    public void showNextQuestion(Question question, Response response) {
        fragment = WalkthroughContentFragment.newInstance(question, response);
        currentContentPresenter = new WalkthroughContentPresenter(fragment);
        presenterStack.push(currentContentPresenter);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction
                .setCustomAnimations(
                        R.animator.slide_in_left,
                        R.animator.slide_out_left,
                        R.animator.slide_in_right,
                        R.animator.slide_out_right)
                .replace(R.id.walkthrough_container, fragment)
                .addToBackStack("walkthrough_question")
                .commit();
    }

    @Override
    public void showPreviousQuestion() {
        fragmentManager.popBackStack();
        presenterStack.pop();
        currentContentPresenter = presenterStack.peek();
    }

    @Override
    public Response getCurrentResponse() {
        return currentContentPresenter.getResponse();
    }

    @Override
    public WalkthroughContentFragment getCurrentFragment() {
        return fragment;
    }

    @Override
    public void showQuestionCount(int index, int total) {
        TextView questionCount = (TextView) findViewById(R.id.question_count);
        String text = "Question: " + (index + 1) + " of " + total;
        questionCount.setText(text);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "WalkthroughActivity is aware of photo taken");
        Response response = currentContentPresenter.getResponse();
        Log.d(TAG, "Got response from fragment: " + response.toString());
        presenter.refreshDisplay(response.getImagePath());
    }
}
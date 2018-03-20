package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ehanna2 on 2/24/2018.
 * Updated by Robert Beerman on 3/18/2018
 */

public class WalkthroughPresenter implements WalkthroughContract.Presenter {
    private static final String TAG = "WalkthroughPresenter";
    private WalkthroughContentFragment walkthroughFragment;
    private WalkthroughContract.View view;
    private List<Question> questions = new ArrayList<>(0);
    private List<Response> responses = new ArrayList<>(0);
    private int currentIndex = 0;
    private int locationId;
    private int walkthroughId;
    private int totalQuestions = 0;
    private int totalResponses = 0;
    private Walkthrough walkthrough;
    private GetWalkthrough getWalkthrough;

    public WalkthroughPresenter(WalkthroughContract.View view) {
        this.view = view;
    }

    @Override
    public void start(int locationId) {
        if (questions.size() == 0) {
            loadQuestions(locationId);
            this.locationId = locationId;
            new GetCurrentWalkthroughIdTask(this).execute();
        }
        GetTotalQuestionCount totalQC = new GetTotalQuestionCount(this);
        totalQC.execute();
    }

    @Override
    public void loadQuestions(int locationId) {
       new WalkthroughQuestionModel(locationId, view, this).execute();
    }

    @Override
    public void loadResponses(int walkthroughId) {
        this.walkthroughId = walkthroughId;
        GetWalkthrough getWalkthrough = new GetWalkthrough(this, String.valueOf(walkthroughId));
        getWalkthrough.execute();
        AsyncTask<Void, Void, List<Response>> wrmAsyncTask = new WalkthroughResponseModel(this.locationId, walkthroughId, view, this).execute();

        try {
            responses = wrmAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (Response response : responses) {
            response.setIsPersisted(true);
        }

        Log.d(TAG, "responses.size(): " + responses.size());
        view.showNextQuestion(questions.get(0), responses);
    }

    @Override
    public void previousQuestionClicked() {
        if(currentIndex == 0) {
            view.showConfirmationDialog();
            return;
        }

        Response currentResponse = view.getCurrentResponse();
        if (currentIndex == responses.size()) {
            responses.add(currentResponse);
        }

        currentIndex--;

        view.showPreviousQuestion();
        view.showQuestionCount(currentIndex, questions.size());
    }

    @Override
    public void nextQuestionClicked() {
        Response response = view.getCurrentResponse();
        response = setUpResponse(response);

        //check if the next question has already been started
        if (currentIndex == responses.size()) {
            responses.add(response);
        }

        //if you're at the last question
        if(currentIndex + 1 == questions.size()) {
            Log.i(TAG, "End of questions, saving " + responses.size() + " responses");
            saveResponses();
            return;
        }

        currentIndex++;

        view.showNextQuestion(questions.get(currentIndex), responses);
        view.showQuestionCount(currentIndex, questions.size());

    }

    private Response setUpResponse(Response response) {
        walkthroughFragment = view.getCurrentFragment();
        response.setWalkthroughId(walkthroughId);
        response.setLocationId(locationId);
        response.setRating(walkthroughFragment.getRating());
        response.setActionPlan(walkthroughFragment.getActionPlan());
        return response;
    }

    @Override
    public void confirmationExitClicked() {
        saveResponses();
        view.closeWalkthrough();
    }

    @Override
    public void backButtonPressed() {
        view.showConfirmationDialog();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        view.showQuestionCount(currentIndex, questions.size());
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    private void saveResponses() {
        SaveResponses save = new SaveResponses();
        save.responses = responses;
        save.execute();
        GetTotalResponseCount totalRC = new GetTotalResponseCount(this);
        totalRC.execute();
        view.closeWalkthrough();
    }

    @Override
    public void setTotalQuestionCount(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    @Override
    public void setTotalResponseCount(int totalResponses) {
        this.totalResponses = totalResponses;
    }

    @Override
    public void setWalkthrough(Walkthrough walkthrough) {
        this.walkthrough = walkthrough;
    }

    @Override
    public void calculateProgress() {
        int percent = 0;
        System.out.println("Total Questions in calculation: " + totalQuestions);
        System.out.println("Total Responses in calculation: " + totalResponses);
        percent = (int) (((double) totalResponses / (double) totalQuestions) * 100);
        System.out.println("Percent complete is: " + percent);

        String id = String.valueOf(walkthroughId);
        walkthrough.setPercentComplete(percent);
        System.out.println(walkthrough.getName());
        System.out.println(walkthrough.getPercentComplete());
    }

    static class SaveResponses extends AsyncTask<Void,Void, Boolean> {
        List<Response> responses;

        @Override
        protected Boolean doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            ResponseDao responseDao = db.responseDao();
            responseDao.insertAll(responses);

            return true;
        }

    }

    @Override
    public void refreshDisplay(String imagePath) {
        Response response = view.getCurrentResponse();
        response.setImagePath(imagePath);
        setUpResponse(response);
    }
}
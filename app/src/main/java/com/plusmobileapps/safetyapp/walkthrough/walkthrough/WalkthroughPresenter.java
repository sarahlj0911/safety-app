package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ehanna2 on 2/24/2018.
 */

public class WalkthroughPresenter implements WalkthroughContract.Presenter {

    private WalkthroughContentFragment walkthroughFragment;
    private WalkthroughContract.View view;
    private List<Question> questions = new ArrayList<>(0);
    private List<Response> responses = new ArrayList<>(0);
    private int currentIndex = 0;
    private int locationId;
    private int walkthroughId;

    public WalkthroughPresenter(WalkthroughContract.View view) {
        this.view = view;
    }

    @Override
    public void start(int locationId) {
       loadQuestions(locationId);
       this.locationId = locationId;
       new GetCurrentWalkthroughIdTask(this).execute();
    }

    @Override
    public void loadQuestions(int locationId) {
       new WalkthroughQuestionModel(locationId, view, this).execute();
    }

    @Override
    public void loadResponses(int walkthroughId) {
        this.walkthroughId = walkthroughId;
//        TODO: insert previous responses from previously filled out walkthroughs
//        new WalkthroughResponseModel(this.locationId, walkthroughId, view, this).execute();
    }

    @Override
    public void previousQuestionClicked() {
        if(currentIndex == 0) {
            view.showConfirmationDialog();
            return;
        }

        Response lastResponse = view.getCurrentResponse();
        lastResponse = setUpResponse(lastResponse);
        responses.set(currentIndex-1, lastResponse);
        currentIndex--;

        view.showPreviousQuestion();
    }

    @Override
    public void nextQuestionClicked() {
        //if you're at the last question
        if(currentIndex + 1 == questions.size()) {
            saveResponses();
            return;
        }

        Response response = view.getCurrentResponse();
        response = setUpResponse(response);

        if(response.isResponseFilledOut()) {
            view.showError(response.isPriorityEmpty(), response.isRatingEmpty());
            return;
        }


        //check if the next question has already been started
        if(currentIndex == responses.size()) {
            responses.add(response);
        } else {
            responses.set(currentIndex, response);
        }
        currentIndex++;
        view.showNextQuestion(questions.get(currentIndex));

    }

    private Response setUpResponse(Response response) {
        response.setWalkthroughId(walkthroughId);
        response.setLocationId(locationId);
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
    }

    public void setResponses(List<Response> responses) { this.responses = responses; }

    private void saveResponses() {

        SaveResponses save = new SaveResponses();
        save.responses = responses;
        save.execute();
        view.closeWalkthrough();
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
}

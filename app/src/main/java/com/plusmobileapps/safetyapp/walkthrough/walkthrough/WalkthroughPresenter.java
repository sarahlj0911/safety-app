package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.ResponseUniqueIdFactory;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    }

    @Override
    public void loadQuestions(int locationId) {
       new WalkthroughQuestionModel(locationId, view, this).execute();
    }

    @Override
    public void loadResponses(int walkthroughId) {
        this.walkthroughId = walkthroughId;

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
        view.showNextQuestion(questions.get(0), getNextResponseToShow());
    }

    private Response getNextResponseToShow() {
        if ( (currentIndex >= 0) && (currentIndex < responses.size()) ) {
            return responses.get(currentIndex);
        } else {
            return new Response();
        }
    }

    @Override
    public void saveQuestions() {
        Response response = getCurrentResponse();
        responses.add(response);
        saveResponses(false);
    }

    @Override
    public void previousQuestionClicked() {
        if(currentIndex == 0) {
            view.showConfirmationDialog();
            return;
        }

        Response currentResponse = getCurrentResponse();
        if (currentIndex == responses.size()) {
            responses.add(currentResponse);
        } else{
            responses.set(currentIndex, currentResponse);
        }

        currentIndex--;

        view.showPreviousQuestion();
        view.showQuestionCount(currentIndex, questions.size());
    }

    @Override
    public void nextQuestionClicked() {
        Response response = getCurrentResponse();

        //check if response has already been added
        if(responses.size() == currentIndex + 1) {
            responses.set(currentIndex, response);
        } else if (currentIndex == responses.size()) {
            responses.add(response);
        }

        //if you're at the last question
        if(currentIndex + 1 == questions.size()) {
            Log.i(TAG, "End of questions, saving " + responses.size() + " responses");
            saveResponses(true);
            return;
        }


        currentIndex++;
        view.showNextQuestion(questions.get(currentIndex), getNextResponseToShow());
        view.showQuestionCount(currentIndex, questions.size());

    }

    private Response getCurrentResponse() {
        Response response = view.getCurrentResponse();
        if(response.getResponseId() == 0) {
            response.setResponseId(ResponseUniqueIdFactory.getId());
            response.setWalkthroughId(walkthroughId);
            response.setLocationId(locationId);
            response.setQuestionId(questions.get(currentIndex).getQuestionId());
        }
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        response.setTimeStamp(timeStamp);
        return response;
    }

    @Override
    public void confirmationExitClicked() {
        saveResponses(true);
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

    private void saveResponses(boolean finish) {

        SaveResponses save = new SaveResponses();
        save.responses = responses;
        save.execute();
        if(finish) {
            view.closeWalkthrough();
        }
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
        Response response = getCurrentResponse();
        response.setImagePath(imagePath);
    }
}
package com.plusmobileapps.safetyapp.walkthrough.walkthrough;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughContentFragment;

import java.util.List;

public interface WalkthroughContract {

    interface View {
        void showConfirmationDialog();
        void showNextQuestion(Question question, List<Response> responses);
        void showPreviousQuestion();
        Response getCurrentResponse();
        WalkthroughContentFragment getCurrentFragment();
        void showQuestionCount(int index, int total);
        void closeWalkthrough();

    }

    interface Presenter {
        void start(int locationId);
        void loadQuestions(int locationId);
        void loadResponses(int walkthroughId);
        void previousQuestionClicked();
        void nextQuestionClicked();
        void confirmationExitClicked();
        void backButtonPressed();
        void refreshDisplay(String imagePath);
        void setTotalQuestionCount(int totalQuestionCount);
        void setTotalResponseCount(int totalResponses);
        void setWalkthrough(Walkthrough walkthrough);
        void calculateProgress();
    }

}

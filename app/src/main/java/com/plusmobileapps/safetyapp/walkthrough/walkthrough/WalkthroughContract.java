package com.plusmobileapps.safetyapp.walkthrough.walkthrough;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;

public interface WalkthroughContract {

    interface View {
        void showConfirmationDialog();
        void showNextQuestion(Question question);
        void showPreviousQuestion();
        Response getCurrentResponse();
        void closeWalkthrough();
        void showError(boolean showPriority, boolean showRating);

    }

    interface Presenter {

        void start(int locationId);

        void loadQuestions(int locationId);
        void loadResponses(int walkthroughId);
        void previousQuestionClicked();
        void nextQuestionClicked();

        void confirmationExitClicked();
        void backButtonPressed();

    }

}

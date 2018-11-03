package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;


import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.model.Priority;

public interface WalkthroughFragmentContract {

    interface View extends BaseView<Presenter> {
        void enableActionPlan(boolean show);

        void showRating(int rating);

        void showPriority(Priority priority);

        void showActionPlan(String actionPlan);

        void showPhoto(String imagePath);

        Response getResponse();

        Response getLoadedResponse();
    }

    interface Presenter extends BasePresenter {
        Response getResponse();

        void priorityClicked(Priority priority);

        void photoTaken(String imagePath);
    }

}

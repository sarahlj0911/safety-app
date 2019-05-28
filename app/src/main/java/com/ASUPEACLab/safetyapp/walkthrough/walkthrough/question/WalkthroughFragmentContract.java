package com.ASUPEACLab.safetyapp.walkthrough.walkthrough.question;


import com.ASUPEACLab.safetyapp.BasePresenter;
import com.ASUPEACLab.safetyapp.BaseView;
import com.ASUPEACLab.safetyapp.data.entity.Response;
import com.ASUPEACLab.safetyapp.model.Priority;

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

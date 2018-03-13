package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;

import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.model.Priority;

/**
 * Created by asteinme on 2/24/18.
 */

public class WalkthroughContentPresenter implements WalkthroughFragmentContract.Presenter {


    private WalkthroughFragmentContract.View view;

    public WalkthroughContentPresenter(WalkthroughFragmentContract.View view ) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
    }


    @Override
    public void priorityClicked(Priority priority) {
        switch (priority) {
            case HIGH:
                view.enableActionPlan(true);
                view.showPriority(priority);
                break;
            case MEDIUM:
                view.enableActionPlan(true);
                view.showPriority(priority);
                break;
            case NONE:
                view.enableActionPlan(false);
                view.showPriority(priority);
                break;
            default:
                break;
        }
    }

    @Override
    public void showError(boolean showPriority, boolean showRating) {
        view.showError(showPriority, showRating);
    }

    @Override
    public void photoTaken() {

    }

    @Override
    public Response getResponse() {

        return view.getResponse();
    }
}

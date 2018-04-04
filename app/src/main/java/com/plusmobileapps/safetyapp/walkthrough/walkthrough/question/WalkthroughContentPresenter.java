package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;

import android.util.Log;

import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.model.Priority;

/**
 * Created by asteinme on 2/24/18.
 * Updated by Robert Beerman on 3/19/18
 */

public class WalkthroughContentPresenter implements WalkthroughFragmentContract.Presenter {

    private static final String TAG = "WalkthruContentPrsntr";
    private WalkthroughFragmentContract.View view;

    Response response;

    public WalkthroughContentPresenter(WalkthroughFragmentContract.View view ) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        response = view.getLoadedResponse();
        Log.d(TAG, "Current response: " + response.toString());

        if (response.isActionItem()) {
            view.enableActionPlan(true);
        }

        if(response.getPriority() != -1) {
            view.showPriority(Priority.values()[response.getPriority()]);
        }
        if(!response.getActionPlan().equals("")) {
            view.showActionPlan(response.getActionPlan());
        }
        if(response.getRating() != -1) {
            view.showRating(response.getRating());
        }

        if (response.getImagePath() != null && !response.getImagePath().trim().equals("")) {
            view.showPhoto(response.getImagePath());
        }
    }

    @Override
    public void priorityClicked(Priority priority) {
        response.setIsPersisted(true);
        switch (priority) {
            case HIGH:
                response.setIsActionItem(1);
                view.enableActionPlan(true);
                view.showPriority(priority);
                break;
            case MEDIUM:
                response.setIsActionItem(1);
                view.enableActionPlan(true);
                view.showPriority(priority);
                break;
            case NONE:
                response.setIsActionItem(0);
                view.enableActionPlan(false);
                view.showPriority(priority);
                break;
            default:
                break;
        }
    }

    @Override
    public void photoTaken(String imagePath) {
        response.setImagePath(imagePath);
    }

    @Override
    public Response getResponse() {

        return view.getResponse();
    }
}

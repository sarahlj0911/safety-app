package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;

import android.util.Log;

import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.model.Priority;

/**
 * Created by asteinme on 2/24/18.
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
        response = getResponse();
        Log.d(TAG, "Current response priority: " + response.getPriority());
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
    public void photoTaken() {

    }

    @Override
    public Response getResponse() {

        return view.getResponse();
    }
}

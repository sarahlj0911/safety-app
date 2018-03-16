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
    private boolean prioritySelected;
    private String photoPath;

    public WalkthroughContentPresenter(WalkthroughFragmentContract.View view ) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        response = getResponse();
        Log.d(TAG, "Current response priority: " + response.getPriority());

        if (response.isActionItem()) {
            view.enableActionPlan(true);
        }

        if (prioritySelected) {
            view.showPriority(Priority.values()[response.getPriority()]);
        }
    }


    @Override
    public void priorityClicked(Priority priority) {
        prioritySelected = true;
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
    public void photoTaken() {

    }

    @Override
    public Response getResponse() {

        return view.getResponse();
    }


}

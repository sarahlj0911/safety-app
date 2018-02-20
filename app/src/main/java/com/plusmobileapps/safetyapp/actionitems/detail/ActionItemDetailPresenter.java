package com.plusmobileapps.safetyapp.actionitems.detail;

import android.os.AsyncTask;
import android.widget.Switch;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class ActionItemDetailPresenter implements ActionItemDetailContract.Presenter {

    private ActionItemDetailContract.View view;
    private Response originalResponse;
    private Response editedResponse;

    public ActionItemDetailPresenter(ActionItemDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void start(String id) {
        new LoadActionItemDetailTask(id, listener).execute();
    }

    @Override
    public void backButtonClicked() {
        if(isActionItemEdited()) {
            view.showConfirmationExitDialog();
        } else {
            view.finishActivity();
        }
    }

    @Override
    public void editPriorityButtonClicked() {
        view.showPriorityDialog();
    }

    @Override
    public void editPriorityPicked(String selectedPriority) {
        int drawable = getStatusColorDrawable(selectedPriority);
        view.changeStatusDot(drawable);
    }

    private int getStatusColorDrawable(String selectedPriority) {
        switch (selectedPriority) {
            case "High":
                editedResponse.setPriority(3);
                return R.drawable.circle_red;
            case "Medium":
                editedResponse.setPriority(2);
                return R.drawable.circle_yellow;
            case "Low":
                editedResponse.setPriority(1);
                return R.drawable.circle_green;
            default:
                return -1;
        }
    }

    private int getStatusColorDrawable(int priority) {
        switch (priority) {
            case 1:
                return R.drawable.circle_green;
            case 2:
                return R.drawable.circle_yellow;
            case 3:
                return R.drawable.circle_red;
            default:
                return -1;
        }
    }

    private void setupUi() {
        view.showActionItem(originalResponse);
        view.changeStatusDot(getStatusColorDrawable(originalResponse.getPriority()));
    }

    @Override
    public void startError() {
        view.finishActivity();
    }

    @Override
    public void saveButtonClicked() {

        if(isActionItemEdited()) {
            new SaveActionItemDetailTask(editedResponse, view).execute();
        } else {
            view.finishActivity();
        }
    }

    private boolean isActionItemEdited() {
        String actionItemPlan = view.getActionItemPlan();
        editedResponse.setActionPlan(actionItemPlan);
        return !editedResponse.equals(originalResponse);
    }

    private ResponseLoadingListener listener = new ResponseLoadingListener() {
        @Override
        public void onResponseLoaded(Response response) {
            originalResponse = response;
            editedResponse = new Response(
                    response.getResponseId(),
                    response.getIsActionItem(),
                    response.getLocationId(),
                    response.getTimeStamp(),
                    response.getRating(),
                    response.getPriority(),
                    response.getActionPlan(),
                    response.getQuestionId(),
                    response.getImagePath(),
                    response.getUserId(),
                    response.getWalkthroughId());
            setupUi();
        }
    };

    interface ResponseLoadingListener {
        void onResponseLoaded(Response response);
    }


}
package com.plusmobileapps.safetyapp.actionitems.detail;

public class ActionItemDetailPresenter implements ActionItemDetailContract.Presenter {

    private ActionItemDetailContract.View view;

    public ActionItemDetailPresenter(ActionItemDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void backButtonClicked() {

    }

    @Override
    public void editPriorityButtonClicked() {
        view.showPriorityDialog();
    }

    @Override
    public void saveButtonClicked() {

    }
}

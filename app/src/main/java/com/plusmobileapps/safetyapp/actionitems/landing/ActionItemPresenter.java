package com.plusmobileapps.safetyapp.actionitems.landing;


import com.plusmobileapps.safetyapp.data.ActionItem;

import java.util.ArrayList;

public class ActionItemPresenter implements ActionItemContract.UserActionsListener {

    private final ActionItemContract.View actionItemView;

    public ActionItemPresenter(ActionItemContract.View actionItemView) {
        this.actionItemView = actionItemView;
    }

    /**
     * initial load of data for action items
     *
     * @param forceUpdate   boolean flag to force refresh data
     */
    @Override
    public void loadActionItems(boolean forceUpdate) {
        actionItemView.setProgressIndicator(true);
        if (forceUpdate) {
            //force a refresh of the data here
        }

        //this is where you would grab the data asynchronously
        new LoadActionItemTask(actionItemView).execute();
    }

    /**
     * load requested action item that user clicks in ActionItemDetailActivity
     *
     * @param requestedActionItem
     */
    @Override
    public void openActionItemDetail(ActionItem requestedActionItem) {
        actionItemView.showActionItemDetailUi("2");
    }
}

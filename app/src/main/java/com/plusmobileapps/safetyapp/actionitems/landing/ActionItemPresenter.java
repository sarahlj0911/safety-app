package com.plusmobileapps.safetyapp.actionitems.landing;


//import com.plusmobileapps.safetyapp.data.ActionItem;
import com.plusmobileapps.safetyapp.data.Response;

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
        //wrapping these next two lines in the onLoadComplete callback
        //actionItemView.setProgressIndicator(false);
        //actionItemView.showActionItems(actionItems);
    }

    /**
     * load requested action item that user clicks in ActionItemDetailActivity
     *
     * @param requestedActionItem
     */
    @Override
    public void openActionItemDetail(Response requestedActionItem) {
        actionItemView.showActionItemDetailUi("2");
    }
}

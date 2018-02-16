package com.plusmobileapps.safetyapp.actionitems.landing;


//import com.plusmobileapps.safetyapp.data.ActionItem;
import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.ArrayList;
import java.util.List;

public class ActionItemPresenter implements ActionItemContract.Presenter {

    private final ActionItemContract.View actionItemView;
    private List<Response> actionItems;
    private boolean isFirstLaunch = true;

    public ActionItemPresenter(ActionItemContract.View actionItemView) {
        this.actionItemView = actionItemView;
        actionItemView.setPresenter(this);
    }

    @Override
    public void start() {
        loadActionItems(isFirstLaunch);
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
            isFirstLaunch = false;
            new LoadActionItemTask(actionItemView, actionItems).execute();
        } else if (actionItems != null) {
            actionItemView.showActionItems(actionItems);
        }

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

package com.plusmobileapps.safetyapp.actionitems.landing;


//import com.plusmobileapps.safetyapp.data.ActionItem;
import com.plusmobileapps.safetyapp.actionitems.detail.SaveActionItemDetailTask;
import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.ArrayList;
import java.util.List;

public class ActionItemPresenter implements ActionItemContract.Presenter {

    private final ActionItemContract.View actionItemView;
    private List<Response> actionItems = new ArrayList<>(0);
    private boolean isFirstLaunch = true;
    private Response lastDismissedResponse;
    private int lastDismissedResponseIndex;

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
            showActionItems();
        }
    }

    @Override
    public void openActionItemDetail(int position) {
        String responseId = Integer.toString(actionItems.get(position).getResponseId());
        actionItemView.showActionItemDetailUi(responseId);
    }

    private void showActionItems() {
        List<Response> responses = new ArrayList<>(0);
        responses.addAll(actionItems);
        actionItemView.showActionItems(responses);
    }

    @Override
    public void dismissButtonClicked(int position) {
        lastDismissedResponse = actionItems.get(position);
        lastDismissedResponseIndex = position;
        updateLastResponse(0);
        actionItems.remove(position);
        actionItemView.dismissActionItem(position);
        showActionItems();
    }

    @Override
    public void undoDismissal() {
        actionItems.add(lastDismissedResponseIndex, lastDismissedResponse);
        actionItemView.restoreActionItem(lastDismissedResponseIndex, lastDismissedResponse);
        updateLastResponse(1);
    }

    private void updateLastResponse(int isActionItem) {
        lastDismissedResponse.setIsActionItem(isActionItem);
        new DismissActionItemTask(lastDismissedResponse, this).execute();
    }
}

package com.plusmobileapps.safetyapp.actionitems.landing;


import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.ArrayList;
import java.util.List;

public class ActionItemPresenter implements ActionItemContract.Presenter {

    private final ActionItemContract.View view;
    private List<Response> actionItems = new ArrayList<>(0);
    private Response lastDismissedResponse;
    private int lastDismissedResponseIndex;

    public ActionItemPresenter(ActionItemContract.View actionItemView) {
        this.view = actionItemView;
        actionItemView.setPresenter(this);
    }

    @Override
    public void start() {
        loadActionItems(true);
    }

    @Override
    public void loadActionItems(boolean forceUpdate) {
        if (forceUpdate) {
            new LoadActionItemTask(this, actionItems).execute();
        } else {
            showActionItems();
        }
        updateNoActionItemText();
    }

    @Override
    public void openActionItemDetail(int position) {

        //this is how to access the data for each action items
        String responseId = Integer.toString(actionItems.get(position).getResponseId());
        view.showActionItemDetailUi(responseId);
    }

    private void showActionItems() {

        List<Response> responses = new ArrayList<>(0);

        responses.addAll(actionItems);
        view.showActionItems(responses);
    }

    @Override
    public void dismissButtonClicked(int position) {
        lastDismissedResponse = actionItems.get(position);
        lastDismissedResponseIndex = position;
        actionItems.remove(position);
        updateLastResponse(Response.NOT_ACTION_ITEM);
        updateNoActionItemText();
        view.dismissActionItem(position);
    }

    @Override
    public void undoDismissal() {
        actionItems.add(lastDismissedResponseIndex, lastDismissedResponse);
        updateNoActionItemText();
        view.restoreActionItem(lastDismissedResponseIndex, lastDismissedResponse);
        updateLastResponse(Response.IS_ACTION_ITEM);
    }

    private void updateLastResponse(int isActionItem) {
        lastDismissedResponse.setIsActionItem(isActionItem);
        new DismissActionItemTask(lastDismissedResponse).execute();
    }

    private void updateNoActionItemText() {
        view.showNoActionItems(actionItems.size() == 0);
    }

    public void setActionItems(List<Response> actionItems) {
        this.actionItems = actionItems;
    }
}

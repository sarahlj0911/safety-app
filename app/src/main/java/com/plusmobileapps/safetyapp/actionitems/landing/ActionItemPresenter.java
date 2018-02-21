package com.plusmobileapps.safetyapp.actionitems.landing;


//import com.plusmobileapps.safetyapp.data.ActionItem;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.util.CopyListUtil;

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
            actionItemView.showActionItems(CopyListUtil.copyResponseList(actionItems));
        }

    }

    @Override
    public void openActionItemDetail(int position) {
        String responseId = Integer.toString(actionItems.get(position).getResponseId());
        actionItemView.showActionItemDetailUi(responseId);
    }

    @Override
    public void dismissButtonClicked(int position) {
        if(lastDismissedResponse != null) {
            updateLastResponse();
        }
        lastDismissedResponse = actionItems.get(position);
        lastDismissedResponseIndex = position;
        actionItems.remove(position);
        actionItemView.dismissActionItem(position);
    }

    @Override
    public void undoDismissal() {
        actionItems.add(lastDismissedResponseIndex, lastDismissedResponse);
        actionItemView.restoreActionItem(lastDismissedResponseIndex, lastDismissedResponse);
        lastDismissedResponse = null;
    }

    @Override
    public void onDestroy() {
        updateLastResponse();
    }

    private void updateLastResponse() {
        //TODO: Create UpdateActionItemTask
        lastDismissedResponse.setIsActionItem(0);
    }
}

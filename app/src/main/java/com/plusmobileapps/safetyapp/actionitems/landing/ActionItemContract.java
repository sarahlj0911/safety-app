package com.plusmobileapps.safetyapp.actionitems.landing;

import com.plusmobileapps.safetyapp.data.Response;

import java.util.List;

public interface ActionItemContract {

    interface View {
        void setProgressIndicator(boolean active);
        void showActionItems(List<Response> actionItems);
        void showActionItemDetailUi(String actionItemId);
    }

    interface UserActionsListener {
        void loadActionItems(boolean forceUpdate);
        void openActionItemDetail(Response requestedActionItem);
    }
}

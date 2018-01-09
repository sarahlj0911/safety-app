package com.plusmobileapps.safetyapp.actionitems.landing;

import java.util.ArrayList;

public interface ActionItemContract {

    interface View {
        void setProgressIndicator(boolean active);
        void showActionItems(ArrayList<ActionItem> actionItems);
        void showActionItemDetailUi(String actionItemId);
    }

    interface UserActionsListener {
        void loadActionItems(boolean forceUpdate);
        void openActionItemDetail(ActionItem requestedActionItem);
    }
}

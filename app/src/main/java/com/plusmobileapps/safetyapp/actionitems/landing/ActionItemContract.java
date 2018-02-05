package com.plusmobileapps.safetyapp.actionitems.landing;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

import java.util.ArrayList;

public interface ActionItemContract {

    interface View extends BaseView<Presenter> {
        void setProgressIndicator(boolean active);
        void showActionItems(ArrayList<ActionItem> actionItems);
        void showActionItemDetailUi(String actionItemId);
    }

    interface Presenter extends BasePresenter {
        void loadActionItems(boolean forceUpdate);
        void openActionItemDetail(ActionItem requestedActionItem);
    }
}

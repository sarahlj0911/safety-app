package com.plusmobileapps.safetyapp.actionitems.landing;

import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.List;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;



public interface ActionItemContract {

    interface View extends BaseView<Presenter> {
        void setProgressIndicator(boolean active);
        void showActionItems(List<Response> actionItems);
        void showActionItemDetailUi(String actionItemId);
        void dismissActionItem(int position);
        void restoreActionItem(int position, Response response);
    }

    interface Presenter extends BasePresenter {
        void loadActionItems(boolean forceUpdate);
        void openActionItemDetail(int position);
        void dismissButtonClicked(int position);
        void undoDismissal();
    }
}

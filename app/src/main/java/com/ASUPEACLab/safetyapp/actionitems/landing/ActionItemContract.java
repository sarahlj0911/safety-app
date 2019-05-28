package com.ASUPEACLab.safetyapp.actionitems.landing;

import com.ASUPEACLab.safetyapp.data.entity.Response;

import java.util.List;

import com.ASUPEACLab.safetyapp.BasePresenter;
import com.ASUPEACLab.safetyapp.BaseView;


public interface ActionItemContract {

    interface View extends BaseView<Presenter> {
        void setProgressIndicator(boolean active);

        void showNoActionItems(boolean show);

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

        void setActionItems(List<Response> actionItems);
    }
}

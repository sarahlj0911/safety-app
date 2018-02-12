package com.plusmobileapps.safetyapp.actionitems.detail;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.data.entity.Response;

public interface ActionItemDetailContract {

    interface View {
        //present the response data onto the UI
        void showActionItem(Response response);

        //present the edit priority dialog to the user
        void showPriorityDialog();

        //grab all editable fields from the ui
        Response getActionItemDetails();
    }

    interface Presenter extends BasePresenter {

        //respond to back button press or arrow in the action bar
        void backButtonClicked();

        //user clicked edit priority button
        void editPriorityButtonClicked();

        //user clicked the save button
        void saveButtonClicked();
    }

}

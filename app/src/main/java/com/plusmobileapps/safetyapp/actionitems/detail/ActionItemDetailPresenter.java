package com.plusmobileapps.safetyapp.actionitems.detail;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class ActionItemDetailPresenter implements ActionItemDetailContract.Presenter {

    private ActionItemDetailContract.View view;
    private Response response;

    public ActionItemDetailPresenter(ActionItemDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void start(String id) {
        new LoadActionItemDetailTask(id, view).execute();
    }

    @Override
    public void backButtonClicked() {
        view.finishActivity();
    }

    @Override
    public void editPriorityButtonClicked() {
        view.showPriorityDialog();
    }

    @Override
    public void editPriorityPicked(String selectedPriority) {
        int drawable = getStatusColorDrawable(selectedPriority);
        //TODO: Update the priority on the response object
        view.changeStatusDot(drawable);
    }

    private int getStatusColorDrawable(String selectedPriority) {
        switch (selectedPriority) {
            case "High":
                return R.drawable.circle_red;
            case "Medium":
                return R.drawable.circle_yellow;
            case "Low":
                return R.drawable.circle_green;
            default:
                return -1;
        }
    }

    @Override
    public void startError() {
        view.finishActivity();
    }

    @Override
    public void saveButtonClicked() {
        String actionItemPlan = view.getActionItemPlan();
        //TODO: uncomment and delete finish activity once we get a valid response object
        //response.setActionPlan(actionItemPlan);
        //new SaveActionItemDetailTask(response, view).execute();
        view.finishActivity();
    }

    static private class LoadActionItemDetailTask extends AsyncTask<Void, Void, Response> {

        private AppDatabase db;
        private String actionItemId;
        private ActionItemDetailContract.View view;

        public LoadActionItemDetailTask(String id, ActionItemDetailContract.View view) {
            actionItemId = id;
            this.view = view;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            ResponseDao dao = db.responseDao();
            Response response = dao.getByResponseId(actionItemId);
            //TODO: verify that response if not null
            //return response;
            return new Response(0,1,1,"11:34pm",3, 2, "Fix it", 2, null,1);
        }

        @Override
        protected void onPostExecute(Response response) {
            view.showActionItem(response);
            db.close();
        }
    }

    static private class SaveActionItemDetailTask extends  AsyncTask<Void, Void, Void> {

        private AppDatabase db;
        private Response response;
        private ActionItemDetailContract.View view;

        public SaveActionItemDetailTask(Response response, ActionItemDetailContract.View view) {
            this.response = response;
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            ResponseDao dao = db.responseDao();
            dao.insertAll(response);
            //TODO: Verify that the response edits were saved
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            db.close();
            view.finishActivity();
        }
    }
}

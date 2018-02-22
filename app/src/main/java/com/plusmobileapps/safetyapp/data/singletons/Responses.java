package com.plusmobileapps.safetyapp.data.singletons;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class Responses  {

    private AppDatabase db;
    private ResponseDao responseDao;
    private List<Response> responses;
    private List<Response> actionItems;

    private static final Responses ourInstance = new Responses();

    public static Responses getInstance() {
        return ourInstance;
    }



    private Responses() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        responseDao = db.responseDao();
        populateResponses();
        populateActionItems();
    }

    private void populateResponses() {
        responses = responseDao.getAll();
    }

    private void populateActionItems() {
        actionItems = responseDao.getAllActionItems();
    }

    // Public Methods.
    public Response getResponseWithId(int responseId) {
        for (int i = 0; i < responses.size(); i++) {
            Response tempResponse = responses.get(i);
            if (tempResponse.getResponseId() == responseId) {
                return tempResponse;
            }
        }
        //Not sure what the appropriate solution in Java is for this.
        return null;
    }

    public List<Response> getResponses() {
        return this.responses;
    }

    public List<Response> getActionItems() {
        return this.actionItems;
    }

    public void addResponse(Response response) {
        responses.add(response);
        if (response.getIsActionItem() == 1) {
            actionItems.add(response);
        }

        try {
            responseDao.insert(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;

/**
 * Created by asteinme on 2/24/18.
 */

public class WalkthroughContentPresenter implements WalkthroughFragmentContract.Presenter {


    private WalkthroughFragmentContract.View view;
    private Response response;

    public WalkthroughContentPresenter(WalkthroughFragmentContract.View view ) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
    }


    @Override
    public Response getResponse() {
        //TODO: get all the values of the priority, action plan
        return response;
    }
}

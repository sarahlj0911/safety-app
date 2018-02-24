package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

/**
 * Created by ehanna2 on 2/24/2018.
 */

public class WalkthroughPresenter implements WalkthroughContract.Presenter {

    private WalkthroughContentFragment walkthroughFragment;
    private Response response;

    public WalkthroughPresenter(Response response) {
        this.response = response;
    }



}

package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;


import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;

public interface WalkthroughFragmentContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        Response getResponse();
    }

}

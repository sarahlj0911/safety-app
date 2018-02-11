package com.plusmobileapps.safetyapp;

import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemsFragment;
import com.plusmobileapps.safetyapp.summary.landing.SummaryFragment;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingFragment;

public class FragmentFactory {
    private static FragmentFactory instance = null;
    private WalkthroughLandingFragment walkthroughLandingFragment;
    private ActionItemsFragment actionItemsFragment;
    private SummaryFragment summaryFragment;

    private FragmentFactory() {
        walkthroughLandingFragment = WalkthroughLandingFragment.newInstance();
        actionItemsFragment = ActionItemsFragment.newInstance();
        summaryFragment = SummaryFragment.newInstance();
    }

    public static FragmentFactory getInstance(){
        if(instance == null) {
            instance = new FragmentFactory();
        }

        return instance;
    }

    public WalkthroughLandingFragment getWalkthroughLandingFragment() {
        return walkthroughLandingFragment;
    }

    public ActionItemsFragment getActionItemsFragment() {
        return actionItemsFragment;
    }

    public SummaryFragment getSummaryFragment() {
        return summaryFragment;
    }

}
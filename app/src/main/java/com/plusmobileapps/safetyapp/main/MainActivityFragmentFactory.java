package com.plusmobileapps.safetyapp.main;

import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemsFragment;
import com.plusmobileapps.safetyapp.summary.landing.SummaryFragment;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingFragment;

public class MainActivityFragmentFactory {
    private WalkthroughLandingFragment walkthroughLandingFragment;
    private ActionItemsFragment actionItemsFragment;
    private SummaryFragment summaryFragment;

    public MainActivityFragmentFactory() {
        walkthroughLandingFragment = WalkthroughLandingFragment.newInstance();
        actionItemsFragment = ActionItemsFragment.newInstance();
        summaryFragment = SummaryFragment.newInstance();
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
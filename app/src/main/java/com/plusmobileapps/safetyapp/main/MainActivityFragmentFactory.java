package com.plusmobileapps.safetyapp.main;

import com.plusmobileapps.safetyapp.AdminOptions.AdminOptionsFragment;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemsFragment;
import com.plusmobileapps.safetyapp.summary.landing.SummaryFragment;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingFragment;

import static com.plusmobileapps.safetyapp.AdminOptions.AdminOptionsFragment.*;

public class MainActivityFragmentFactory {
    private static MainActivityFragmentFactory instance = null;
    private WalkthroughLandingFragment walkthroughLandingFragment;
    private ActionItemsFragment actionItemsFragment;
    private SummaryFragment summaryFragment;
    private AdminOptionsFragment adminOptionsFragment;

    public MainActivityFragmentFactory() {
        walkthroughLandingFragment = WalkthroughLandingFragment.newInstance();
        actionItemsFragment = ActionItemsFragment.newInstance();
        adminOptionsFragment = AdminOptionsFragment.newInstance();
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

    public AdminOptionsFragment getAdminOptionsFragment() {return adminOptionsFragment;}

}
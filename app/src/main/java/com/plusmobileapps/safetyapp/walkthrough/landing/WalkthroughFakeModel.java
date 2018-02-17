package com.plusmobileapps.safetyapp.walkthrough.landing;

import java.util.ArrayList;

public class WalkthroughFakeModel {

    private ArrayList<WalkthroughOverview> walkthroughs;

    public WalkthroughFakeModel() {
        walkthroughs = new ArrayList<>(0);
        populateData();
    }

    public ArrayList<WalkthroughOverview> getWalkthroughs() {
        return walkthroughs;
    }

    /**
     * Create mock data
     */
    private void populateData() {

        //WalkthroughOverview walkthrough1 = new WalkthroughOverview("Spring 2017");
        WalkthroughOverview walkthrough2 = new WalkthroughOverview("Fall 2017", "Sep 3, 2017", "4:33 p.m.");
        walkthrough2.setWalkthroughId(0L);
        WalkthroughOverview walkthrough3 = new WalkthroughOverview("Summer 2017", "Sep 3, 2017", "4:33 p.m.");
        walkthrough3.setWalkthroughId(1L);

        WalkthroughOverview walkthrough1 = new WalkthroughOverview("Fall 2017", "Dec 12, 2017", "3:33 p.m.");
        walkthrough1.setWalkthroughId(2L);
        walkthrough1.setProgress(50);
        walkthroughs.add(walkthrough1);
        walkthroughs.add(walkthrough2);
        walkthroughs.add(walkthrough3);
    }





}

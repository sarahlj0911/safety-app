package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughOverview;

import java.util.ArrayList;

public class FakeSummaryModel {

    private ArrayList<WalkthroughOverview> walkthroughs;

    public FakeSummaryModel() {
        walkthroughs = new ArrayList<>();
        walkthroughs = new ArrayList<>();
        WalkthroughOverview walkthrough2 = new WalkthroughOverview("Fall 2017");
        walkthrough2.setDate("Oct. 4, 2017\n1:30 p.m.");

        WalkthroughOverview walkthrough3 = new WalkthroughOverview("Summer 2017");
        walkthrough3.setDate("Jul. 9, 2017\n9:00 a.m.");

        WalkthroughOverview walkthrough1 = new WalkthroughOverview("Spring 2017");
        walkthrough1.setDate("Mar. 3, 2017\n11:30 a.m.");

        walkthroughs.add(walkthrough2);
        walkthroughs.add(walkthrough3);
        walkthroughs.add(walkthrough1);
    }

    public ArrayList<WalkthroughOverview> getSummaries(){
        return walkthroughs;
    }
}

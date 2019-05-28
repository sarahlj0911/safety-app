package com.ASUPEACLab.safetyapp.summary.landing;

import java.util.ArrayList;

import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;

public class FakeSummaryModel {

    private ArrayList<Walkthrough> walkthroughs;

    public FakeSummaryModel() {
        walkthroughs = new ArrayList<>();
        walkthroughs = new ArrayList<>();
        Walkthrough walkthrough2 = new Walkthrough("Fall 2017");
        walkthrough2.setCreatedDate("Oct. 4, 2017\n1:30 p.m.");

        Walkthrough walkthrough3 = new Walkthrough("Summer 2017");
        walkthrough3.setCreatedDate("Jul. 9, 2017\n9:00 a.m.");

        Walkthrough walkthrough1 = new Walkthrough("Spring 2017");
        walkthrough1.setCreatedDate("Mar. 3, 2017\n11:30 a.m.");

        walkthroughs.add(walkthrough2);
        walkthroughs.add(walkthrough3);
        walkthroughs.add(walkthrough1);
    }

    public ArrayList<Walkthrough> getSummaries() {
        return walkthroughs;
    }
}

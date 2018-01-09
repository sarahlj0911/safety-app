package com.plusmobileapps.safetyapp.actionitems.landing;

import java.util.ArrayList;

/**
 * This is just a fake model to generate data
 * TODO: Create the central repository pattern for one entry point into accessing data
 */
public class FakeModel {
    private ArrayList<ActionItem> actionItems;

    public FakeModel() {
        ActionItem actionItem = new ActionItem("Graffiti", "Boys Bathroom", 1, "The boys got a hold of some sharpies and really did a number on the bathroom stall. There was some profanity written that needs to be removed immediately. I don't know what kind of monster would write such a thing.");
        actionItems = new ArrayList<ActionItem>(0);
        for (int i = 0; i < 20; i++) {
            actionItems.add(actionItem);
        }
    }

    public ArrayList<ActionItem> getActionItems() {

        return actionItems;
    }
}

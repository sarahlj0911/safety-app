package com.plusmobileapps.safetyapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.plusmobileapps.safetyapp.actionitems.landing.view.ActionItemsFragment;
import com.plusmobileapps.safetyapp.summary.SummaryFragment;
import com.plusmobileapps.safetyapp.surveys.RootFragment;

/**
 * Created by Andrew on 10/28/2017.
 */

public class MainSwipeAdapter extends FragmentPagerAdapter {

    private ActionItemsFragment actionFragment;
    private SummaryFragment summaryFragment;

    public MainSwipeAdapter(FragmentManager fm) {
        super(fm);
        actionFragment = new ActionItemsFragment();
        summaryFragment = new SummaryFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RootFragment();
            case 1:
                return actionFragment;
            case 2:
                return summaryFragment;
            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

package com.plusmobileapps.safetyapp.summary.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class SummaryOverviewDetailsSwipeAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "SwipeAdapter";

    public SummaryOverviewDetailsSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //return new SummaryOverviewFragment();
                return SummaryOverviewFragment.newInstance();
            case 1:
                return new SummaryDetailsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}

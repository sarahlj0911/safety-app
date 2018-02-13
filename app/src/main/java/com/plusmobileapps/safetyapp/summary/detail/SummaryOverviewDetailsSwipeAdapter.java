package com.plusmobileapps.safetyapp.summary.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SummaryOverviewDetailsSwipeAdapter extends FragmentPagerAdapter {

    public SummaryOverviewDetailsSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SummaryOverviewFragment();
            case 1:
                return new SummaryDetailsFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}

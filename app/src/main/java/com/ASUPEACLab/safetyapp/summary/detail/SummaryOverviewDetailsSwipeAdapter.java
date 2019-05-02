package com.ASUPEACLab.safetyapp.summary.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SummaryOverviewDetailsSwipeAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SODSwipeAdapter";

    SummaryOverviewDetailsSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.d(TAG, "Loading new overview fragment instance");
                return SummaryOverviewFragment.newInstance();
            case 1:
                Log.d(TAG, "Loading new details fragment instance");
                return SummaryDetailsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}

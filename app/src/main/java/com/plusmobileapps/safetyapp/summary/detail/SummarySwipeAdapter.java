package com.plusmobileapps.safetyapp.summary.detail;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SummarySwipeAdapter extends FragmentPagerAdapter {
    private TabDetailFragment tabDetailFragment;
    private TabSummaryFragment tabSummaryFragment;

    public SummarySwipeAdapter(FragmentManager fm) {
        super(fm);
        tabDetailFragment = new TabDetailFragment();
        tabSummaryFragment = new TabSummaryFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return tabDetailFragment;
            case 1:
                return tabSummaryFragment;
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

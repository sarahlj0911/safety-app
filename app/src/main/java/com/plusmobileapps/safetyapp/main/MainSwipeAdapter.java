package com.plusmobileapps.safetyapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainSwipeAdapter extends FragmentPagerAdapter {

    private MainActivityFragmentFactory factory;

    public MainSwipeAdapter(FragmentManager fm, MainActivityFragmentFactory factory) {
        super(fm);
        this.factory = factory;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return factory.getWalkthroughLandingFragment();
            case 1:
                return factory.getActionItemsFragment();
            case 2:
                return factory.getSummaryFragment();
            default:
                break;
        }

        return null;
    }

    @Override
    //was 3 now 2 since we are disabling the summary for MVP
    public int getCount() {
        return 2;
    }

}

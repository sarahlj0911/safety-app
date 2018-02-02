package com.plusmobileapps.safetyapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.plusmobileapps.safetyapp.FragmentFactory;

public class MainSwipeAdapter extends FragmentPagerAdapter {

    public MainSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FragmentFactory.getInstance().getSurveyLandingFragment();
            case 1:
                return FragmentFactory.getInstance().getActionItemsFragment();
            case 2:
                return FragmentFactory.getInstance().getSummaryFragment();
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

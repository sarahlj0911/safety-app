package com.plusmobileapps.safetyapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.plusmobileapps.safetyapp.actionitems.DashboardFragment;
import com.plusmobileapps.safetyapp.summary.SummaryFragment;
import com.plusmobileapps.safetyapp.survey.SurveyFragment;

/**
 * Created by Andrew on 10/28/2017.
 */

public class SwipeAdapter extends FragmentPagerAdapter {
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SurveyFragment();
            case 1:
                return new DashboardFragment();
            case 2:
                return new SummaryFragment();
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

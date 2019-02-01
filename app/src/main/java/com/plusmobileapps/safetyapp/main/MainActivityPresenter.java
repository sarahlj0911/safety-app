package com.plusmobileapps.safetyapp.main;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.plusmobileapps.safetyapp.R;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void navButtonClicked(int position) {
        view.changePage(position);
    }

    @Override
    public void pageSwipedTo(int position) {
        switch (position) {
            case 0:
                view.changeNavHighlight(R.id.navigation_walkthrough);
                break;
            case 1:
                view.changeNavHighlight(R.id.navigation_dashboard);
                break;
            //Removed summary for MVP
            //  case 2:
            //      view.changeNavHighlight(R.id.navigation_history);
            //      break;
            default:
                break;
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_walkthrough:
                    presenter.navButtonClicked(0);
                    return true;
                case R.id.navigation_dashboard:
                    presenter.navButtonClicked(1);
                    return true;
                //case R.id.navigation_history:
                //    presenter.navButtonClicked(2);
                //    return true;
            }
            return false;
        }

    };

    @Override
    public void backButtonPressed() {
    }
}

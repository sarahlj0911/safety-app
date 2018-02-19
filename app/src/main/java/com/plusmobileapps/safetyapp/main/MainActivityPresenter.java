package com.plusmobileapps.safetyapp.main;


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
            case 2:
                view.changeNavHighlight(R.id.navigation_history);
                break;
            default:
                break;
        }
    }

    @Override
    public void backButtonPressed() {
    }
}

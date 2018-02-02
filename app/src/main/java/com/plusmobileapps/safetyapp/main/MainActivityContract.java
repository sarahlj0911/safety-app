package com.plusmobileapps.safetyapp.main;

public interface MainActivityContract {

    interface View {
        void changePage(int position);
        void changeNavHighlight(int position);
    }

    interface Presenter {
        void navButtonClicked(int position);
        void pageSwipedTo(int position);
        void backButtonPressed();
    }
}

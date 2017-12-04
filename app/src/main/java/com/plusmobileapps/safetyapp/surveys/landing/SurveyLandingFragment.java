package com.plusmobileapps.safetyapp.surveys.landing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.surveys.location.LocationFragment;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class SurveyLandingFragment extends Fragment{
    private static final String TAG = "SurveyLandingFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected SurveyLandingFragment.LayoutManagerType currentLayoutManagerType;
    protected SurveyLandingAdapter adapter;

    private ArrayList<SurveyOverview> surveys;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public SurveyLandingFragment() {
        // Required empty public constructor
    }


    public static SurveyLandingFragment newInstance() {
        SurveyLandingFragment fragment = new SurveyLandingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_survey_landing, container, false);
        rootView.setTag(TAG);
        recyclerView = rootView.findViewById(R.id.landing_survey_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        populateSurveyItems();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SurveyLandingAdapter(surveys);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        for (int i = 0; i < surveys.size(); i++) {
            adapter.notifyItemInserted(i);
        }

        return rootView;
    }


    private void populateSurveyItems() {
        surveys = new ArrayList<>();

        //SurveyOverview survey1 = new SurveyOverview("Spring 2017");
        SurveyOverview survey2 = new SurveyOverview("Fall 2017");
        SurveyOverview survey3 = new SurveyOverview("Summer 2017");

        SurveyOverview survey1 = new SurveyOverview("Fall 2017", "Dec 12, 2017", "3:33 p.m.");
        survey1.setProgress(50);
        surveys.add(survey1);
        surveys.add(survey2);
        surveys.add(survey3);
    }

    public boolean isSurveyInProgress(){
        for (SurveyOverview survey : surveys) {
            if (survey.isInProgress()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public String getSurveyTitle(int position) {
        if (position > 2){
            return surveys.get(position-2).getTitle();
        } else {
            return surveys.get(position-1).getTitle();
        }
    }

}

package com.plusmobileapps.safetyapp.surveys.landing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.surveys.location.LocationFragment;

import java.util.ArrayList;

public class SurveyLandingFragment extends Fragment {
    private static final String TAG = "SurveyLandingFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected SurveyLandingFragment.LayoutManagerType currentLayoutManagerType;
    protected SurveyLandingAdapter adapter;
    private ArrayList<LandingSurveyOverview> surveys;

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

        recyclerView.setLayoutManager(layoutManager);
        populateSurveyItems();
        adapter = new SurveyLandingAdapter(surveys);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private void populateSurveyItems() {
        surveys = new ArrayList<>();
        LandingSurveyOverview survey1 = new LandingSurveyOverview("Spring 2017");
        LandingSurveyOverview survey2 = new LandingSurveyOverview("Fall 2017");
        LandingSurveyOverview survey3 = new LandingSurveyOverview("Summer 2017");

        surveys.add(survey1);
        surveys.add(survey2);
        surveys.add(survey3);
    }

}

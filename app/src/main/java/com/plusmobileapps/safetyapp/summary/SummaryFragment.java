package com.plusmobileapps.safetyapp.summary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyOverview;

import java.util.ArrayList;

public class SummaryFragment extends Fragment {
    private static final String TAG = "SummaryFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected SummaryFragment.LayoutManagerType currentLayoutManagerType;
    protected SummaryAdapter adapter;
    private ArrayList<SurveyOverview> surveys;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        rootView.setTag(TAG);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.summary_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        recyclerView.setLayoutManager(layoutManager);

        surveys = new ArrayList<>();
        populateSurveys();
        adapter = new SummaryAdapter(surveys);

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void populateSurveys(){
        surveys = new ArrayList<>();
        SurveyOverview survey2 = new SurveyOverview("Fall 2017");
        survey2.setDate("Oct. 4, 2017\n1:30 p.m.");

        SurveyOverview survey3 = new SurveyOverview("Summer 2017");
        survey3.setDate("Jul. 9, 2017\n9:00 a.m.");

        SurveyOverview survey1 = new SurveyOverview("Spring 2017");
        survey1.setDate("Mar. 3, 2017\n11:30 a.m.");

        surveys.add(survey2);
        surveys.add(survey3);
        surveys.add(survey1);
    }
}

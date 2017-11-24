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
    private ArrayList<LandingSurveyOverview> surveys;
    private int removePosition = 0;
    private LandingSurveyOverview landingSurveyOverview = new LandingSurveyOverview();

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
        LandingSurveyOverview survey2 = new LandingSurveyOverview("Spring 2017", "May 12, 2017", "12:23 p.m.");
        LandingSurveyOverview survey3 = new LandingSurveyOverview("Summer 2017", "Aug 13, 2017", "2:23 p.m.");

        LandingSurveyOverview survey1 = new LandingSurveyOverview("Fall 2017", "Dec 12, 2017", "3:33 p.m.");
        survey1.setProgress(50);
        surveys.add(survey1);
        surveys.add(survey2);
        surveys.add(survey3);
    }

    public boolean isSurveyInProgress(){
        for (LandingSurveyOverview survey : surveys) {
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
        return surveys.get(position).getTitle();
    }

    public void dismissSurvey(final int position) {
        removePosition = position;
        landingSurveyOverview = surveys.get(position);
        surveys.remove(position);
        adapter.notifyItemRemoved(position);
        Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.fragment_survey_landing),
                "Survey was dismissed", Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
        mySnackbar.setCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
            }
        });
    }

    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            surveys.add(removePosition, landingSurveyOverview);
            adapter.notifyItemInserted(removePosition);
        }
    }

}

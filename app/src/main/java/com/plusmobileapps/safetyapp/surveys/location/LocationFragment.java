package com.plusmobileapps.safetyapp.surveys.location;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    private static final String TAG = "SurveyViewFragment";    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected LayoutManagerType currentLayoutManagerType;
    protected LocationAdapter adapter;
    ArrayList<LocationSurveyOverview> surveys = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    private boolean isCompleted = false;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance(boolean isCompleted) {
        LocationFragment fragment = new LocationFragment();
        fragment.isCompleted = isCompleted;
        fragment.populateSurveys();
        return fragment;
    }

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        fragment.createNewSurvey();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);
        rootView.setTag(TAG);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.survey_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(currentLayoutManagerType);

        adapter = new LocationAdapter(surveys);

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public void landingSurveyClicked(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    //populate array list
    private void populateSurveys(){
        addLocations();

        if (isCompleted){
            for (int i =0; i < titles.size(); i++){
                surveys.add(new LocationSurveyOverview(titles.get(i)));
                surveys.get(i).setFinished(true);
            }
        } else {
            for (int i = 0; i < titles.size(); i++) {
                surveys.add(new LocationSurveyOverview(titles.get(i)));
                int progress = 90;
                if (i == 0){
                    //no op to keep progress state at 0
                } else if(i % 2 ==1){
                    surveys.get(i).setFinished(true);
                } else {
                    surveys.get(i).setProgress(progress);
                    progress -= 10 ;
                }
            }
        }
    }

    private void addLocations(){
        titles.add("Bathroom");
        titles.add("Classroom 1");
        titles.add("Bathroom 2");
        titles.add("Locker Room");
        titles.add("Field");
        titles.add("Office");
        titles.add("Classroom 2");
        titles.add("Bathroom");
        titles.add("Classroom 1");
        titles.add("Bathroom 2");
        titles.add("Locker Room");
        titles.add("Field");
        titles.add("Office");
        titles.add("Classroom 2");
    }

    private void createNewSurvey() {
        addLocations();
        for (String title : titles) {
            surveys.add(new LocationSurveyOverview(title));
        }
    }
}

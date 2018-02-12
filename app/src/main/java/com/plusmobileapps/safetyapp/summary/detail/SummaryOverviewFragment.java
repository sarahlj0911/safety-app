package com.plusmobileapps.safetyapp.summary.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;


public class SummaryOverviewFragment extends Fragment implements SummaryOverviewContract.View {
    private static final String TAG = "SummaryOverviewFragment";
    private SummaryOverviewContract.Presenter presenter;

    public SummaryOverviewFragment() {
        // Required empty public constructor
    }

    public static SummaryOverviewFragment newInstance() {
        SummaryOverviewFragment fragment = new SummaryOverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (presenter == null) {
            Log.d(TAG, "presenter is null");
        } else {
            Log.d(TAG, "presenter is NOT null");
        }

        this.presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_summary_overview, container, false);
    }

    @Override
    public void setPresenter(SummaryOverviewContract.Presenter presenter) {
        Log.d(TAG, "Setting presenter: " + presenter.getClass().toString());
        this.presenter = presenter;
    }

}

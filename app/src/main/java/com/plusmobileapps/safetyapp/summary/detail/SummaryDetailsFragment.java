package com.plusmobileapps.safetyapp.summary.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;


public class SummaryDetailsFragment extends Fragment implements SummaryDetailsContract.View {
    private static final String TAG = "SummaryDetailsFragment";
    private SummaryDetailsContract.Presenter presenter;

    public SummaryDetailsFragment() {
        // Required empty public constructor
    }

    public static SummaryDetailsFragment newInstance() {
        SummaryDetailsFragment fragment = new SummaryDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this.presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_summary_details, container, false);
    }

    @Override
    public void setPresenter(SummaryDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }
}

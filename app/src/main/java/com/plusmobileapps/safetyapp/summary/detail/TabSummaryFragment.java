package com.plusmobileapps.safetyapp.summary.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;


public class TabSummaryFragment extends Fragment {

    public TabSummaryFragment() {
        // Required empty public constructor
    }

    public static TabSummaryFragment newInstance() {
        TabSummaryFragment fragment = new TabSummaryFragment();

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
        return inflater.inflate(R.layout.fragment_tab_summary, container, false);
    }

}

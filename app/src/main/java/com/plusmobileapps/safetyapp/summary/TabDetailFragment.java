package com.plusmobileapps.safetyapp.summary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;


public class TabDetailFragment extends Fragment {

    public TabDetailFragment() {
        // Required empty public constructor
    }


    public static TabDetailFragment newInstance() {
        TabDetailFragment fragment = new TabDetailFragment();
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
        return inflater.inflate(R.layout.fragment_tab_detail, container, false);
    }
}

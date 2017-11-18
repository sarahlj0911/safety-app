package com.plusmobileapps.safetyapp.surveys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingAdapter;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyLandingFragment;
import com.plusmobileapps.safetyapp.surveys.location.LocationFragment;


public class RootFragment extends Fragment {
    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.root_frame, new SurveyLandingFragment());

        transaction.commit();

        return view;
    }
}

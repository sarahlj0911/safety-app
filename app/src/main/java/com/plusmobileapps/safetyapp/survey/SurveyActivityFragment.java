package com.plusmobileapps.safetyapp.survey;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyActivityFragment extends Fragment {

    public SurveyActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_one, container, false);

        return view;
    }
}

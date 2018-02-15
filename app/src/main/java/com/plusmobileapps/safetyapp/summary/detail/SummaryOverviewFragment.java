package com.plusmobileapps.safetyapp.summary.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;


public class SummaryOverviewFragment extends Fragment implements SummaryOverviewContract.View {
    private static final String TAG = "SummaryOverviewFragment";
    private SummaryOverviewContract.Presenter presenter;
    private TextView titleTextView;

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
        //this.presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_summary_overview, container, false);

        if (view != null) {
            Log.d(TAG, "View is not null!!!");
            titleTextView = (TextView) view.findViewById(R.id.textview_summary_overview);
        }
        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_tab_summary_overview, container, false);
    }

    public void setPresenter(SummaryOverviewContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void showTitle(String title) {
        Log.d(TAG, "Showing title: " + title);

        titleTextView.setText(title);

    }
}

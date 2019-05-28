package com.ASUPEACLab.safetyapp.summary.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ASUPEACLab.safetyapp.R;

public class SummaryOverviewFragment extends Fragment implements SummaryOverviewContract.View {
    private static final String TAG = "SummaryOverviewFragment";
    private SummaryOverviewContract.Presenter presenter;
    private TextView titleTextView;

    public SummaryOverviewFragment() {
        // Required empty public constructor
    }

    public static SummaryOverviewFragment newInstance() {
        Log.d(TAG, "Creating new instance of SummaryOverviewFragment");
        return new SummaryOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_summary_overview, container, false);

        if (view != null) {
            titleTextView = view.findViewById(R.id.textview_summary_overview);
        }

        return view;
    }

    public void setPresenter(SummaryOverviewContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void showTitle(String title) {
        titleTextView.setText(title);
    }
}

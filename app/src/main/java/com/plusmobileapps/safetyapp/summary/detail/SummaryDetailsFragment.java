package com.plusmobileapps.safetyapp.summary.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;


public class SummaryDetailsFragment extends Fragment implements SummaryDetailsContract.View {
    private static final String TAG = "SummaryDetailsFragment";
    private SummaryDetailsContract.Presenter presenter;
    private TextView titleTextView;

    public SummaryDetailsFragment() {
        // Required empty public constructor
    }

    public static SummaryDetailsFragment newInstance() {
        return new SummaryDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_tab_summary_details, container, false);

        if (view != null) {
            titleTextView = view.findViewById(R.id.textview_summary_details);
        }

        return view;
    }

    @Override
    public void setPresenter(SummaryDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void showTitle(String title) {
        titleTextView.setText(title);
    }
}

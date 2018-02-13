package com.plusmobileapps.safetyapp.summary.landing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.summary.detail.SummaryOverviewDetailsActivity;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughOverview;

import java.util.ArrayList;

public class SummaryFragment extends Fragment implements SummaryContract.View {
    private static final String TAG = "SummaryFragment";
    protected RecyclerView recyclerView;
    protected SummaryAdapter adapter;
    private ArrayList<WalkthroughOverview> walkthroughs;

    private SummaryContract.Presenter presenter;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        rootView.setTag(TAG);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.summary_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SummaryAdapter(new ArrayList<WalkthroughOverview>(0), itemListener);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void setPresenter(SummaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showSummaries(ArrayList<WalkthroughOverview> summaries) {
        adapter.replaceData(summaries);
    }

    @Override
    public void showSummaryDetailUi(WalkthroughOverview summary) {
        Log.d(TAG, "Walkthrough title: " + summary.getTitle());
        Intent intent = new Intent(getContext(), SummaryOverviewDetailsActivity.class);
        intent.putExtra("walkthroughTitle", summary.getTitle());
        startActivity(intent);
    }

    /**
     * Handle clicks of recyclerview
     */
    SummaryItemListener itemListener = new SummaryItemListener() {
        @Override
        public void onSummaryItemClicked(WalkthroughOverview summary) {
            presenter.openSummary(summary);
        }
    };

    /**
     * Interface for summary items being clicked in recyclerview
     */
    public interface SummaryItemListener {
        void onSummaryItemClicked(WalkthroughOverview summary);
    }

}

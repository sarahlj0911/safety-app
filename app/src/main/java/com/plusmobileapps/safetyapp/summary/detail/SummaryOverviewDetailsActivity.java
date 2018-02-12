package com.plusmobileapps.safetyapp.summary.detail;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.plusmobileapps.safetyapp.FragmentFactory;
import com.plusmobileapps.safetyapp.R;

public class SummaryOverviewDetailsActivity extends AppCompatActivity {

    private static final String TAG = "SummaryOvrviewDtlsAct";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String walkthroughTitle;

    /*private SummaryOverviewContract.Presenter summaryOverviewPresenter;
    private SummaryDetailsContract.Presenter summaryDetailsPresenter;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);
        walkthroughTitle = getIntent().getExtras().getString("walkthroughTitle");

        Toolbar toolbar = findViewById(R.id.summary_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(walkthroughTitle);
        viewPager = findViewById(R.id.summary_view_pager);
        tabLayout = findViewById(R.id.summary_detail_tabs);
        final SummaryOverviewDetailsSwipeAdapter swipeAdapter = new SummaryOverviewDetailsSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

        SummaryOverviewFragment summaryOverviewFragment = (SummaryOverviewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_tab_summary_overview);

        if (summaryOverviewFragment == null) {
            summaryOverviewFragment = SummaryOverviewFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_tab_summary_overview, summaryOverviewFragment);
            transaction.commit();
        }

        SummaryDetailsFragment summaryDetailsFragment = (SummaryDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_tab_summary_details);

        if (summaryDetailsFragment == null) {
            summaryDetailsFragment = SummaryDetailsFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_tab_summary_details, summaryDetailsFragment);
            transaction.commit();
        }

        SummaryOverviewContract.Presenter summaryOverviewPresenter = new SummaryOverviewPresenter(summaryOverviewFragment, walkthroughTitle);
        SummaryDetailsContract.Presenter summaryDetailsPresenter = new SummaryDetailsPresenter(summaryDetailsFragment);

        /*final SummaryOverviewDetailsSwipeAdapter swipeAdapter = new SummaryOverviewDetailsSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);*/

        /*swipeAdapter.setSummaryOverviewFragment(summaryOverviewFragment);
        swipeAdapter.setSummaryDetailsFragment(summaryDetailsFragment);*/

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTab = tab.getPosition();
                switch (selectedTab) {
                    case 0:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1, true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}


package com.plusmobileapps.safetyapp.summary.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.plusmobileapps.safetyapp.R;

public class SummaryOverviewDetailsActivity extends AppCompatActivity {

    private static final String TAG = "SummaryOvrviewDtlsAct";

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);
        TabLayout tabLayout;
        long walkthroughId;

        String walkthroughTitle;

        walkthroughId = getIntent().getExtras().getInt("walkthroughId");

        // TODO Change to have presenter get the walkthroughTitle by walkthroughId once the data layer's wired up
        walkthroughTitle = getIntent().getExtras().getString("walkthroughTitle");

        Toolbar toolbar = findViewById(R.id.summary_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(walkthroughTitle);
        viewPager = findViewById(R.id.summary_view_pager);
        tabLayout = findViewById(R.id.summary_detail_tabs);

        createFragmentLifecycleListener();

        final SummaryOverviewDetailsSwipeAdapter swipeAdapter = new SummaryOverviewDetailsSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createFragmentLifecycleListener() {
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                super.onFragmentActivityCreated(fm, f, savedInstanceState);
                Log.d(TAG, "Fragment recreated");
                createPresenter(f);
            }
        }, false);
    }

    private void createPresenter(Fragment fragment) {
        if (fragment instanceof SummaryOverviewContract.View) {
            new SummaryOverviewPresenter((SummaryOverviewContract.View) fragment);
        } else if (fragment instanceof SummaryDetailsContract.View){
            new SummaryDetailsPresenter((SummaryDetailsContract.View) fragment);
        }
    }
}
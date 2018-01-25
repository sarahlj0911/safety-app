package com.plusmobileapps.safetyapp.summary.detail;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.plusmobileapps.safetyapp.R;

public class SummaryDetailActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.summary_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Summary");
        viewPager = findViewById(R.id.summary_view_pager);
        tabLayout = findViewById(R.id.summary_detail_tabs);
        final SummarySwipeAdapter swipeAdapter = new SummarySwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

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


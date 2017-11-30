package com.plusmobileapps.safetyapp.summary;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.plusmobileapps.safetyapp.R;

public class SummaryDetailActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);

        viewPager = findViewById(R.id.summary_view_pager);
        final SummarySwipeAdapter swipeAdapter = new SummarySwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

    }
}


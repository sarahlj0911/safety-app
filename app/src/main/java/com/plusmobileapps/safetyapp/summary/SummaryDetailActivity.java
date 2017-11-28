package com.plusmobileapps.safetyapp.summary;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.plusmobileapps.safetyapp.R;

public class SummaryDetailActivity extends AppCompatActivity {
    TabHost host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);

        host = (TabHost) findViewById(R.id.tabhost);
        System.out.println("HOST IS NULL: " + (host == null));
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        host.setup(mLocalActivityManager);
        System.out.println("TABHOST BOTTOM: " + host.getBottom());

        Intent intent;

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("overview");
        spec.setIndicator("overview");
        intent = new Intent(this, OverviewActivity.class);
        spec.setContent(intent);
        System.out.println("SPEC IS NULL: " + (spec == null));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("detail");
        spec.setIndicator("detail");
        intent = new Intent(this, DetailActivity.class);
        spec.setContent(intent);
        host.addTab(spec);
//
//        host.setCurrentTab(1);
////        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
////            @Override
////            public void onTabChanged(String tabId) {
////                // display the name of the tab whenever a tab is changed
////                Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
////            }
////        });
    }
}


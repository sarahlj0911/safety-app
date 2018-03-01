package com.plusmobileapps.safetyapp.walkthrough.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingFragment;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.WalkthroughActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationContract.View{

    public static final String EXTRA_WALKTHROUGH_LOCATION_NAME = "walkthrough_name";
    public static final String EXTRA_WALKTHROUGH_ID = "walkthrough_Id";

    private LocationContract.Presenter presenter;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent intent = getIntent();
        String title = intent.getStringExtra(WalkthroughLandingFragment.EXTRA_WALKTHROUGH_NAME);
        int id = intent.getIntExtra("walkthroughId", -1);//TODO: FIX
        new LocationPresenter(this, id);

        Toolbar toolbar = findViewById(R.id.location_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //will make the back button appear on toolbar

        adapter = new LocationAdapter(new ArrayList<Location>(0), itemListener);
        recyclerView = findViewById(R.id.location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.backButtonPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(LocationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLocations(List<Location> locations) {
        this.locations = locations;
        adapter.setData(locations);
    }

    @Override
    public void openRequestedLocation(int locationId, String locationName, int walkthroughId) {
        //TODO refactor to use locationId.
        Intent intent = new Intent(this, WalkthroughActivity.class);
        intent.putExtra(WalkthroughActivity.EXTRA_LOCATION_ID, locationId);
        intent.putExtra(EXTRA_WALKTHROUGH_LOCATION_NAME, locationName);
        intent.putExtra(EXTRA_WALKTHROUGH_ID, walkthroughId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        presenter.backButtonPressed();
    }

    @Override
    public void navigateBack() {
        finish();
    }

    private LocationItemListener itemListener = new LocationItemListener() {
        @Override
        public void onLocationClicked(Location location) {
            presenter.locationClicked(location);
        }
    };

    public interface LocationItemListener {
        void onLocationClicked(Location location);
    }
}

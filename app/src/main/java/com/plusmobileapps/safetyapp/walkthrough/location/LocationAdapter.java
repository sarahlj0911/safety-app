package com.plusmobileapps.safetyapp.walkthrough.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private static final String TAG = "LocationAdapter";
    public static final String EXTRA_LOCATION = "com.plusmobileapps.safetyapp.location.overview.LOCATION";
    private Location location;
    private LocationActivity.LocationItemListener itemListener;
    private List<Location> locations;

    public LocationAdapter(ArrayList<Location> walkthroughs, LocationActivity.LocationItemListener itemListener){
        this.locations = walkthroughs;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_walkthrough, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        location = locations.get(position);

        holder.getWalkthroughTitle().setText(location.getName());
        holder.getCheckmark().setVisibility(location.isFinished() ? View.VISIBLE : View.INVISIBLE);
        if (location.getProgress() > 0){
            holder.getProgressBar().setVisibility(View.VISIBLE);
            holder.getProgressBar().setProgress(location.getProgress());
        } else {
            holder.getProgressBar().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setData(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView walkthroughTitle;
        private final ProgressBar progressBar;
        private final ImageView checkmark;
        private final boolean finished = false;
        private final int progress = 0;
        private final Context context;

        public ViewHolder(View view) {
            super (view);
            context = itemView.getContext();
            walkthroughTitle = view.findViewById(R.id.viewholder_title_location);
            progressBar = view.findViewById(R.id.viewholder_progressbar_location);
            checkmark = view.findViewById(R.id.viewholder_check_location);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onLocationClicked(locations.get(getAdapterPosition()));
                }
            });
        }
        public TextView getWalkthroughTitle() {
            return walkthroughTitle;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public ImageView getCheckmark(){
            return checkmark;
        }


        public boolean isFinished() {
            return finished;
        }

        public int getProgress() {
            return progress;
        }
    }

}

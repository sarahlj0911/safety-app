package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.ArrayList;

public class WalkthroughLandingAdapter extends RecyclerView.Adapter<WalkthroughLandingAdapter.CardViewHolder> {

    private static final String TAG = "WalkthroughLandingAdapter";
    public static final String EXTRA_WALKTHROUGH = "com.plusmobileapps.safetyapp.walkthrough.landing.WALKTHROUGH";
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_HEADER = 0;
    private Walkthrough walkthrough;
    private int walkthroughCount = 0;
    private WalkthroughLandingFragment.WalkthroughLandingItemListener itemListener;

    private ArrayList<Walkthrough> walkthroughs;

    public WalkthroughLandingAdapter(ArrayList<Walkthrough> walkthroughs, WalkthroughLandingFragment.WalkthroughLandingItemListener itemListener) {
        this.walkthroughs = walkthroughs;
        this.itemListener = itemListener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_landing_walkthrough, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        if(walkthroughs.size() > 0) {
            walkthrough = walkthroughs.get(position);
            TextView header = holder.getHeader();

            //handle the header visibility
            if (position <= 1) {
                header.setVisibility(View.VISIBLE);
                if (position == 1) {
                    header.setText(R.string.viewholder_header_completed);
                } else {
                    header.setText(R.string.viewholder_header_inprogress);
                }
            } else {
                header.setVisibility(View.GONE);
            }

            //TODO: Labels missing
            holder.getDate().setText(walkthrough.getDate(walkthrough.getCreatedDate()));
            holder.getTime().setText(walkthrough.getTime(walkthrough.getLastUpdatedDate()));
            holder.getTitle().setText(walkthrough.getName());

            //handle progress bar
            if (walkthrough.isInProgress()) {
                holder.getModified().setText(walkthrough.getDate(walkthrough.getLastUpdatedDate()));
                holder.getProgressBar().setVisibility(View.VISIBLE);
                holder.getProgressBar().setProgress((int)walkthrough.getPercentComplete());
            } else {
                holder.getProgressBar().setVisibility(View.INVISIBLE);
                holder.getModified().setText(walkthrough.getDate(walkthrough.getLastUpdatedDate()));
            }
        }

    }

    @Override
    public int getItemCount() {
        return walkthroughs.size();
    }

    public void replaceData(ArrayList<Walkthrough> walkthroughs) {
        this.walkthroughs = walkthroughs;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView time;
        private final TextView date;
        private final TextView title;
        private final TextView modified;
        private final ProgressBar progressBar;
        private final TextView header;

        public CardViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.viewholder_landing_title);
            time = view.findViewById(R.id.viewholder_landing_time);
            date = view.findViewById(R.id.viewholder_landing_date);
            modified = view.findViewById(R.id.viewholder_landing_modified);
            progressBar = view.findViewById(R.id.viewholder_landing_progressbar);
            header = view.findViewById(R.id.viewholder_title);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.onWalkthroughClicked(getAdapterPosition());
        }

        public TextView getTime() {
            return time;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getModified() {
            return modified;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public TextView getHeader() {
            return header;
        }

    }

}

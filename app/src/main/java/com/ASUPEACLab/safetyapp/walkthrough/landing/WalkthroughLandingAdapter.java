package com.ASUPEACLab.safetyapp.walkthrough.landing;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ASUPEACLab.safetyapp.R;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;

import java.util.List;

public class WalkthroughLandingAdapter extends RecyclerView.Adapter<WalkthroughLandingAdapter.CardViewHolder> {

    private static final String TAG = "WalkthruLandingAdapter";
    public static final String EXTRA_WALKTHROUGH = "com.plusmobileapps.safetyapp.walkthrough.landing.WALKTHROUGH";
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_HEADER = 0;
    private Walkthrough walkthrough;
    private WalkthroughLandingFragment.WalkthroughLandingItemListener itemListener;

    private List<Walkthrough> walkthroughs;

    public WalkthroughLandingAdapter(List<Walkthrough> walkthroughs, WalkthroughLandingFragment.WalkthroughLandingItemListener itemListener) {
        this.walkthroughs = walkthroughs;
        this.itemListener = itemListener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "In onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_landing_walkthrough, parent, false);
        return new CardViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        Log.d(TAG, "In onBindViewHolder");
        if (walkthroughs.size() > 0) {
            int i = walkthroughs.size() - position - 1;
            walkthrough = walkthroughs.get(i);
            TextView header = holder.getHeader();
            holder.setReversePosition(i);

            if (walkthrough.isInProgress() && position == 0) {
                header.setVisibility(View.VISIBLE);
                header.setText(R.string.viewholder_header_inprogress);
                holder.dismissButton.setVisibility(View.VISIBLE);
                holder.getProgressBar().setVisibility(View.VISIBLE);
                holder.getProgressBar().setProgress((int) walkthrough.getPercentComplete());
            } else {
                header.setText(R.string.viewholder_header_completed);
                holder.dismissButton.setVisibility(View.GONE);
                holder.getProgressBar().setVisibility(View.INVISIBLE);
                if (position == 0) {
                    header.setVisibility(View.VISIBLE);
                } else if (position == 1 && walkthroughs.get(walkthroughs.size() - 1).isInProgress()) {
                    header.setVisibility(View.VISIBLE);
                } else {
                    header.setVisibility(View.GONE);
                }
            }

            holder.getModified().setText(R.string.walkthrough_last_modified);
            holder.getDate().setText(walkthrough.getDate(walkthrough.getLastUpdatedDate()));
            holder.getTime().setText(walkthrough.getTime(walkthrough.getLastUpdatedDate()));
            holder.getTitle().setText(walkthrough.getName());
        }
    }


    @Override
    public int getItemCount() {
        return walkthroughs.size();
    }

    public void replaceData(List<Walkthrough> walkthroughs) {
        this.walkthroughs = walkthroughs;
    }

    public List<Walkthrough> getWalkthroughs() {
        return this.walkthroughs;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView time;
        private final TextView date;
        private final TextView title;
        private final TextView modified;
        private final ProgressBar progressBar;
        private final TextView header;
        private final CheckBox dismissButton;
        private int reversePosition = 0;

        public CardViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.viewholder_landing_title);
            time = view.findViewById(R.id.viewholder_landing_time);
            date = view.findViewById(R.id.viewholder_landing_date);
            modified = view.findViewById(R.id.viewholder_landing_modified);
            progressBar = view.findViewById(R.id.viewholder_landing_progressbar);
            header = view.findViewById(R.id.viewholder_title);
            dismissButton = view.findViewById(R.id.dismissCheckBox);
            dismissButton.setOnClickListener(dismissListener);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.onWalkthroughClicked(getAdapterPosition());
        }

        private View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onDismissButtonClicked(reversePosition, dismissButton);
            }
        };

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

        public CheckBox getDismissButton() {
            return dismissButton;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public TextView getHeader() {
            return header;
        }

        public void setReversePosition(int reversePosition) {
            this.reversePosition = reversePosition;
        }

    }
}

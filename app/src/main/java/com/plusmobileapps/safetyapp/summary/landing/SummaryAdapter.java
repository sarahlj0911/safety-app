package com.plusmobileapps.safetyapp.summary.landing;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughOverview;

import java.util.ArrayList;

import com.plusmobileapps.safetyapp.R;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private static final String TAG = "SummaryAdapter";
    private WalkthroughOverview walkthrough;
    private ArrayList<WalkthroughOverview> summaries;

    private SummaryFragment.SummaryItemListener itemListener;

    public SummaryAdapter(ArrayList<WalkthroughOverview> walkthroughs, SummaryFragment.SummaryItemListener itemListener){
        this.summaries = walkthroughs;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_summary_walkthrough, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        walkthrough = summaries.get(position);

        holder.getDateTime().setText(walkthrough.getDate());
        holder.getTitle().setText(walkthrough.getTitle());

        //TODO: Refactor model to have these data points
        holder.getRedCount().setText("2");
        holder.getGreenCount().setText("4");
        holder.getYellowCount().setText("6");
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

    public void replaceData(ArrayList<WalkthroughOverview> summaries) {
        this.summaries = summaries;
        notifyDataSetChanged();
    }

    private WalkthroughOverview getSummary(int position) {
        return summaries.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title = itemView.findViewById(R.id.walkthrough_title);
        private final TextView dateTime = itemView.findViewById(R.id.date_time);
        private final TextView redCount = itemView.findViewById(R.id.walkthrough_red_count);
        private final TextView greenCount = itemView.findViewById(R.id.walkthrough_green_count);
        private final TextView yellowCount = itemView.findViewById(R.id.walkthrough_yellow_count);

        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            WalkthroughOverview summary = getSummary(getAdapterPosition());
            itemListener.onSummaryItemClicked(summary);
        }

        public TextView getTitle() {
            return title;
        }
        public TextView getDateTime() {
            return dateTime;
        }

        public TextView getRedCount() {
            return redCount;
        }

        public TextView getGreenCount() {
            return greenCount;
        }

        public TextView getYellowCount() {
            return yellowCount;
        }
    }
}

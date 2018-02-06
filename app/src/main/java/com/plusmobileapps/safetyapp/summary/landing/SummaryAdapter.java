package com.plusmobileapps.safetyapp.summary.landing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.summary.detail.SummaryDetailActivity;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyOverview;

import java.util.ArrayList;

import com.plusmobileapps.safetyapp.R;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private static final String TAG = "SummaryAdapter";
    private SurveyOverview survey;
    private ArrayList<SurveyOverview> summaries;

    private SummaryFragment.SummaryItemListener itemListener;

    public SummaryAdapter(ArrayList<SurveyOverview> surveys, SummaryFragment.SummaryItemListener itemListener){
        this.summaries = surveys;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_summary_survey, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        survey = summaries.get(position);

        holder.getDateTime().setText(survey.getDate());
        holder.getTitle().setText(survey.getTitle());

        //TODO: Refactor model to have these data points
        holder.getRedCount().setText("2");
        holder.getGreenCount().setText("4");
        holder.getYellowCount().setText("6");
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

    public void replaceData(ArrayList<SurveyOverview> summaries) {
        this.summaries = summaries;
        notifyDataSetChanged();
    }

    private SurveyOverview getSummary(int position) {
        return summaries.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title = itemView.findViewById(R.id.survey_title);
        private final TextView dateTime = itemView.findViewById(R.id.date_time);
        private final TextView redCount = itemView.findViewById(R.id.survey_red_count);
        private final TextView greenCount = itemView.findViewById(R.id.survey_green_count);
        private final TextView yellowCount = itemView.findViewById(R.id.survey_yellow_count);

        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SurveyOverview summary = getSummary(getAdapterPosition());
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

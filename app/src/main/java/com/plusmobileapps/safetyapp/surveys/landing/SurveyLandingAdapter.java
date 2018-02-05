package com.plusmobileapps.safetyapp.surveys.landing;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;

import java.util.ArrayList;

/**
 * Created by Andrew on 11/13/2017.
 */

public class SurveyLandingAdapter extends RecyclerView.Adapter<SurveyLandingAdapter.CardViewHolder> {

    private static final String TAG = "SurveyLandingAdapter";
    public static final String EXTRA_SURVEY = "com.plusmobileapps.safetyapp.survey.landing.SURVEY";
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_HEADER = 0;
    private SurveyOverview survey;
    private int surveyCount = 0;
    private SurveyLandingFragment.SurveyLandingItemListener itemListener;

    private ArrayList<SurveyOverview> surveys;

    public SurveyLandingAdapter(ArrayList<SurveyOverview> surveys, SurveyLandingFragment.SurveyLandingItemListener itemListener) {
        this.surveys = surveys;
        this.itemListener = itemListener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_landing_survey, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        survey = surveys.get(position);
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

        holder.getDate().setText(survey.getDate());
        holder.getTime().setText(survey.getTime());
        holder.getTitle().setText(survey.getTitle());

        //handle progress bar
        if (survey.isInProgress()) {
            holder.getModified().setText(survey.getModified());
            holder.getProgressBar().setVisibility(View.VISIBLE);
            holder.getProgressBar().setProgress(survey.getProgress());
        } else {
            holder.getProgressBar().setVisibility(View.INVISIBLE);
            holder.getModified().setText(survey.getModified());
        }
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    public void replaceData(ArrayList<SurveyOverview> surveys) {
        this.surveys = surveys;
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
            itemListener.onSurveyClicked(getAdapterPosition());
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

package com.plusmobileapps.safetyapp.surveys.landing;

import android.content.Context;
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

public class SurveyLandingAdapter extends RecyclerView.Adapter<SurveyLandingAdapter.ViewHolder> {

    private static final String TAG = "SurveyLandingAdapter";
    public static final String EXTRA_SURVEY = "com.plusmobileapps.safetyapp.survey.landing.SURVEY";
    private LandingSurveyOverview survey;

    private ArrayList<LandingSurveyOverview> surveys;

    public SurveyLandingAdapter(ArrayList<LandingSurveyOverview> surveys) {
        this.surveys = surveys;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_landing_survey, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        survey = surveys.get(position);

        if(survey.isInProgress()) {
            holder.getProgressBar().setVisibility(View.VISIBLE);
            holder.getModified().setVisibility(View.VISIBLE);
            holder.getTime().setVisibility(View.VISIBLE);
            holder.getDate().setVisibility(View.VISIBLE);
            holder.getProgressBar().setProgress(survey.getProgress());
            holder.getDate().setText(survey.getDate());
            holder.getTime().setText(survey.getTime());
            holder.getTitle().setText(survey.getTitle());
        } else {
            holder.getModified().setVisibility(View.INVISIBLE);
            holder.getTime().setVisibility(View.INVISIBLE);
            holder.getDate().setVisibility(View.INVISIBLE);
            holder.getProgressBar().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        OnSurveySelectedListener mCallback;
        private final TextView time;
        private final TextView date;
        private final TextView title;
        private final TextView modified;
        private final ProgressBar progressBar;

        public interface OnSurveySelectedListener {
            public void onSurveySelected(int position);
        }

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.viewholder_landing_title);
            time = view.findViewById(R.id.viewholder_landing_time);
            date = view.findViewById(R.id.viewholder_landing_date);
            modified = view.findViewById(R.id.viewholder_landing_modified);
            progressBar = view.findViewById(R.id.viewholder_landing_progressbar);
            try{
                mCallback = (OnSurveySelectedListener) itemView.getContext();
            } catch (ClassCastException e){
                throw new ClassCastException(itemView.getContext().toString() + " must implement OnSurveySelectedListener");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onSurveySelected(getAdapterPosition());
                }
            });
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
    }

}

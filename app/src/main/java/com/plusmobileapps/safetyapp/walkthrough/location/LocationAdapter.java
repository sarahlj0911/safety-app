package com.plusmobileapps.safetyapp.walkthrough.location;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.walkthrough.survey.WalkthroughActivity;

import java.util.ArrayList;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private static final String TAG = "LocationAdapter";
    public static final String EXTRA_LOCATION = "com.plusmobileapps.safetyapp.survey.overview.LOCATION";
    private LocationSurveyOverview survey;


    private ArrayList<LocationSurveyOverview> surveys;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView surveyTitle;
        private final ProgressBar progressBar;
        private final ImageView checkmark;
        private final boolean finished = false;
        private final int progress = 0;
        private final Context context;

        public ViewHolder(View view) {
            super (view);
            context = itemView.getContext();
            surveyTitle = view.findViewById(R.id.viewholder_title_survey);
            progressBar = view.findViewById(R.id.viewholder_progressbar_survey);
            checkmark = view.findViewById(R.id.viewholder_check_survey);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), WalkthroughActivity.class);
                    String location = surveyTitle.getText().toString();
                    intent.putExtra(EXTRA_LOCATION, location);
                    context.startActivity(intent);
                }
            });
        }
        public TextView getSurveyTitle() {
            return surveyTitle;
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

    public LocationAdapter(ArrayList<LocationSurveyOverview> surveys){
        this.surveys = surveys;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_survey, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        survey = surveys.get(position);

        holder.getSurveyTitle().setText(survey.getTitle());
        holder.getCheckmark().setVisibility(survey.isFinished() ? View.VISIBLE : View.INVISIBLE);
        if (survey.getProgress() > 0){
            holder.getProgressBar().setVisibility(View.VISIBLE);
            holder.getProgressBar().setProgress(survey.getProgress());
        } else {
            holder.getProgressBar().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }



}

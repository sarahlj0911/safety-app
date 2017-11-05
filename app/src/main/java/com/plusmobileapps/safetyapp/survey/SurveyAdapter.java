package com.plusmobileapps.safetyapp.survey;

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

import java.util.ArrayList;

/**
 * Created by Andrew on 11/4/2017.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

    private static final String TAG = "SurveyAdapter";
    public static final String EXTRA_QUESTION = "com.plusmobileapps.safetyapp.survey.QUESTION";
    private SurveyOverview survey;


    private ArrayList<SurveyOverview> surveys
            ;

    public class ViewHolder extends RecyclerView.ViewHolder {
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
                    Intent intent = new Intent(view.getContext(), SurveyActivity.class);
                    int question = getAdapterPosition();
                    intent.putExtra(EXTRA_QUESTION, question);
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

    public SurveyAdapter(ArrayList<SurveyOverview> surveys){
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
        holder.getProgressBar().setProgress(survey.getProgress());
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }



}

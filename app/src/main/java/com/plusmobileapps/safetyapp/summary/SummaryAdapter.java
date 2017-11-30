package com.plusmobileapps.safetyapp.summary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.plusmobileapps.safetyapp.surveys.landing.SurveyOverview;

import java.util.ArrayList;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.surveys.survey.SurveyActivity;

/**
 * Created by aaronmusengo on 11/25/17.
 */
public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private static final String TAG = "SummaryAdapter";
    private SurveyOverview survey;

    private ArrayList<SurveyOverview> surveys;

    public SummaryAdapter(ArrayList<SurveyOverview> surveys){
        this.surveys = surveys;
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
        survey = surveys.get(position);

        holder.getDateTime().setText(survey.getDate());
        holder.getTitle().setText(survey.getTitle());
    }

    @Override
    public int getItemCount() {
        return surveys.size();
    }

    //The Viewholder is viewholder_summary_survey.xml
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView title = itemView.findViewById(R.id.survey_title);
        private final TextView dateTime = itemView.findViewById(R.id.date_time);

        public ViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //handle the click of the view holder here
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SummaryDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        public TextView getTitle() {
            return title;
        }


        public TextView getDateTime() {
            return dateTime;
        }
    }
}

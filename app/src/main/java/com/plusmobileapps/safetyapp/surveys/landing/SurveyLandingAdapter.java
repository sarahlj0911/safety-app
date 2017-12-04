package com.plusmobileapps.safetyapp.surveys.landing;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import java.util.ArrayList;

/**
 * Created by Andrew on 11/13/2017.
 */

public class SurveyLandingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SurveyLandingAdapter";
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final String EXTRA_SURVEY = "com.plusmobileapps.safetyapp.survey.landing.SURVEY";
    private SurveyOverview survey;
    private int surveyCount = 0;

    private ArrayList<SurveyOverview> surveys;

    public SurveyLandingAdapter(ArrayList<SurveyOverview> surveys) {
        this.surveys = surveys;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 2) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                View titleView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.viewholder_title, parent, false);
                return new TitleViewHolder(titleView);
            case 1:
                View cardViewHolder = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.viewholder_landing_survey, parent, false);
                return new CardViewHolder(cardViewHolder);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
            final int itemType = getItemViewType(position);

            if (itemType == ITEM_TYPE_HEADER){
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                if (position == 0) {
                    titleViewHolder.getTitle().setText("In Progress");
                } else {
                    titleViewHolder.getTitle().setText("Completed");
                }
            } else if (itemType == ITEM_TYPE_NORMAL){
                CardViewHolder cardViewHolder = (CardViewHolder) holder;
                if (position > 2){
                    survey = surveys.get(position-2);
                } else {
                    survey = surveys.get(position-1);
                }
                cardViewHolder.getDate().setText(survey.getDate());
                cardViewHolder.getTime().setText(survey.getTime());
                cardViewHolder.getTitle().setText(survey.getTitle());
                if(survey.isInProgress()) {
                    cardViewHolder.getModified().setText(survey.getModified());
                    cardViewHolder.getProgressBar().setVisibility(View.VISIBLE);
                    cardViewHolder.getProgressBar().setProgress(survey.getProgress());
                } else {
                    cardViewHolder.getProgressBar().setVisibility(View.INVISIBLE);
                    cardViewHolder.getModified().setText(survey.getModified());
                }
                surveyCount++;
            }
    }

    @Override
    public int getItemCount() {
        return (surveys.size() + 2);
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        public TitleViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.viewholder_title);
        }

        public TextView getTitle() {
            return title;
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        OnSurveySelectedListener mCallback;
        private final TextView time;
        private final TextView date;
        private final TextView title;
        private final TextView modified;
        private final ProgressBar progressBar;

        public interface OnSurveySelectedListener {
            public void onSurveySelected(int position);
        }

        public CardViewHolder(View view) {
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

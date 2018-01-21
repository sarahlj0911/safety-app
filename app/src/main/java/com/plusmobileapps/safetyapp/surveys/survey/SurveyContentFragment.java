package com.plusmobileapps.safetyapp.surveys.survey;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plusmobileapps.safetyapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyContentFragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    SurveyQuestion survey;
    ImageButton cameraButton;
    View priorityRed;
    View priorityYellow;
    View priorityGreen;
    View priorityRedSelected;
    View priorityYellowSelected;
    View priorityGreenSelected;

    public static SurveyContentFragment newInstance(SurveyQuestion survey) {
        SurveyContentFragment fragment = new SurveyContentFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("survey", new Gson().toJson(survey));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_question, container, false);
        String surveyJsonObject = getArguments().getString("survey");
        survey = new Gson().fromJson(surveyJsonObject, SurveyQuestion.class);
        TextView description = view.findViewById(R.id.question_description);
        description.setText(survey.getDescription());

        cameraButton = view.findViewById(R.id.button_take_photo);
        cameraButton.setOnClickListener(this);

        priorityRed = view.findViewById(R.id.priority_btn_red);
        priorityRed.setOnClickListener(this);

        priorityYellow = view.findViewById(R.id.priority_btn_yellow);
        priorityYellow.setOnClickListener(this);

        priorityGreen = view.findViewById(R.id.priority_btn_green);
        priorityGreen.setOnClickListener(this);

        priorityRedSelected = view.findViewById(R.id.priority_btn_red_selected);
        priorityRedSelected.setOnClickListener(this);

        priorityYellowSelected = view.findViewById(R.id.priority_btn_yellow_selected);
        priorityYellowSelected.setOnClickListener(this);

        priorityGreenSelected = view.findViewById(R.id.priority_btn_green_selected);
        priorityGreenSelected.setOnClickListener(this);

        return view;
    }

    /**
     * Handle all click events here within the fragment
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_take_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.priority_btn_red:
                Log.d(this.getClass().toString(), "Red Button selected: ");
                /*priorityRed.setMinimumHeight(52);
                priorityRed.setMinimumWidth(52);*/
               /* view.findViewById(R.id.priority_btn_red).setVisibility(View.GONE);
                view.findViewById(R.id.priority_btn_red_selected).setVisibility(View.VISIBLE);*/
                break;
            case R.id.priority_btn_yellow:
                Log.d(this.getClass().toString(), "Yellow Button selected: ");
                /*priorityYellow.setMinimumHeight(52);
                priorityYellow.setMinimumWidth(52);*/
                /*view.findViewById(R.id.priority_btn_yellow).setVisibility(View.GONE);
                view.findViewById(R.id.priority_btn_yellow_selected).setVisibility(View.VISIBLE);*/
                break;
            case R.id.priority_btn_green:
                Log.d(this.getClass().toString(), "Green Button selected: ");
                /*priorityGreen.setMinimumHeight(52);
                priorityGreen.setMinimumWidth(52);*/
                /*view.findViewById(R.id.priority_btn_green).setVisibility(View.GONE);
                view.findViewById(R.id.priority_btn_green_selected).setVisibility(View.VISIBLE);*/
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            cameraButton.setImageBitmap(imageBitmap);
        }
    }
}

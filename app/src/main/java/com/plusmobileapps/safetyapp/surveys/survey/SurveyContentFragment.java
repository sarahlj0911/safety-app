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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plusmobileapps.safetyapp.model.Priority;
import com.plusmobileapps.safetyapp.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyContentFragment extends Fragment implements View.OnClickListener {

    static final String TAG = "SurveyContentFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SurveyQuestion survey;
    ImageButton cameraButton;
    ArrayList<String> options = new ArrayList<>();
    Priority priority;
    View priorityRed;
    View priorityYellow;
    View priorityGreen;
    View priorityRedSelected;
    View priorityYellowSelected;
    View priorityGreenSelected;
    TextView actionPlanLabel;
    EditText actionPlanEditText;

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
        Log.d(TAG, surveyJsonObject);
        survey = new Gson().fromJson(surveyJsonObject, SurveyQuestion.class);
        TextView description = view.findViewById(R.id.question_description);
        description.setText(survey.getDescription());
        actionPlanLabel = view.findViewById(R.id.title_action_plan);
        actionPlanEditText = view.findViewById(R.id.actionPlanEditText);

        options = survey.getOptions();

        populateOptionRadioButtons(view);

        priorityRed = view.findViewById(R.id.priority_btn_red);
        priorityRed.setOnClickListener(this);

        priorityYellow = view.findViewById(R.id.priority_btn_yellow);
        priorityYellow.setOnClickListener(this);

        priorityGreen = view.findViewById(R.id.priority_btn_green);
        priorityGreen.setOnClickListener(this);

        priorityRedSelected = view.findViewById(R.id.priority_btn_red_selected);
        priorityYellowSelected = view.findViewById(R.id.priority_btn_yellow_selected);
        priorityGreenSelected = view.findViewById(R.id.priority_btn_green_selected);

        priority = survey.getPriority();

        loadPriority();

        cameraButton = view.findViewById(R.id.button_take_photo);
        cameraButton.setOnClickListener(this);

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
                priority = Priority.HIGH;
                priorityRed.setVisibility(View.GONE);
                priorityRedSelected.setVisibility(View.VISIBLE);
                priorityYellowSelected.setVisibility(View.GONE);
                priorityYellow.setVisibility(View.VISIBLE);
                priorityGreenSelected.setVisibility(View.GONE);
                priorityGreen.setVisibility(View.VISIBLE);
                actionPlanLabel.setEnabled(true);
                actionPlanEditText.setEnabled(true);
                break;
            case R.id.priority_btn_yellow:
                priority = Priority.MEDIUM;
                priorityYellow.setVisibility(View.GONE);
                priorityYellowSelected.setVisibility(View.VISIBLE);
                priorityRedSelected.setVisibility(View.GONE);
                priorityRed.setVisibility(View.VISIBLE);
                priorityGreenSelected.setVisibility(View.GONE);
                priorityGreen.setVisibility(View.VISIBLE);
                actionPlanLabel.setEnabled(true);
                actionPlanEditText.setEnabled(true);
                break;
            case R.id.priority_btn_green:
                priority = Priority.NONE;
                priorityGreen.setVisibility(View.GONE);
                priorityGreenSelected.setVisibility(View.VISIBLE);
                priorityYellowSelected.setVisibility(View.GONE);
                priorityYellow.setVisibility(View.VISIBLE);
                priorityRedSelected.setVisibility(View.GONE);
                priorityRed.setVisibility(View.VISIBLE);
                actionPlanLabel.setEnabled(false);
                actionPlanEditText.setEnabled(false);
                break;
            default:
                break;
        }

        survey.setPriority(priority);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            cameraButton.setImageBitmap(imageBitmap);
        }
    }

    private void loadPriority() {
        if (priority != null) {
            switch (priority) {
                case HIGH:
                    priorityRed.setVisibility(View.GONE);
                    priorityRedSelected.setVisibility(View.VISIBLE);
                    actionPlanLabel.setEnabled(true);
                    actionPlanEditText.setEnabled(true);
                    break;
                case MEDIUM:
                    priorityYellow.setVisibility(View.GONE);
                    priorityYellowSelected.setVisibility(View.VISIBLE);
                    actionPlanLabel.setEnabled(true);
                    actionPlanEditText.setEnabled(true);
                    break;
                case NONE:
                    priorityGreen.setVisibility(View.GONE);
                    priorityGreenSelected.setVisibility(View.VISIBLE);
                    actionPlanLabel.setEnabled(false);
                    actionPlanEditText.setEnabled(false);
                    break;
                default:
                    Log.d(TAG, "SurveyQuestion has unrecognized priority!");
                    break;
            }
        }
    }

    private void populateOptionRadioButtons(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

        for (int i = 0; i < options.size(); i++) {
            View v = radioGroup.getChildAt(i);
            if (v instanceof RadioButton) {
                ((RadioButton) v).setText(options.get(i));
            }
        }
    }
}

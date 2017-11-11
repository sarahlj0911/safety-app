package com.plusmobileapps.safetyapp.surveys.survey;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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

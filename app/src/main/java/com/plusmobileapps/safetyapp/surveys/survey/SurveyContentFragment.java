package com.plusmobileapps.safetyapp.surveys.survey;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
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
    String currentPhotoPath;
    PackageManager packageManager;

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
        packageManager = this.getActivity().getPackageManager();
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

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraButton.setOnClickListener(this);
        } else {
            cameraButton.setEnabled(false);
        }

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
                    // Create the File where the photo should go
                    File imageFile = null;

                    try {
                        imageFile = createImageFile();
                    } catch (IOException ioe) {
                        Log.d(TAG, "Error while creating image file");
                        ioe.printStackTrace();
                    }

                    if (imageFile != null) {
                        Uri imageURI = FileProvider.getUriForFile(this.getActivity(),
                                "com.plusmobileapps.safetyapp.fileprovider",
                                imageFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                    // startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            int targetWidth = cameraButton.getWidth();
            int targetHeight = cameraButton.getHeight();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            int photoWidth = bmOptions.outWidth;
            int photoHeight = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoWidth/targetWidth, photoHeight/targetHeight);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

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

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + survey.getLocation() + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

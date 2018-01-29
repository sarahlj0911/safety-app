package com.plusmobileapps.safetyapp.surveys.survey;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
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
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private SurveyQuestion survey;
    private TextView descriptionTextView;
    private String description;
    private ImageButton cameraButton;
    private ArrayList<String> options = new ArrayList<>();
    private int rating;
    private Priority priority;
    private View priorityRed;
    private View priorityYellow;
    private View priorityGreen;
    private View priorityRedSelected;
    private View priorityYellowSelected;
    private View priorityGreenSelected;
    private TextView actionPlanLabel;
    private EditText actionPlanEditText;
    private String actionPlan;
    private String photoPath;
    private PackageManager packageManager;

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

        survey = new Gson().fromJson(surveyJsonObject, SurveyQuestion.class);
        descriptionTextView = view.findViewById(R.id.question_description);
        actionPlanLabel = view.findViewById(R.id.title_action_plan);
        actionPlanEditText = view.findViewById(R.id.actionPlanEditText);
        priorityRed = view.findViewById(R.id.priority_btn_red);
        priorityRed.setOnClickListener(this);
        priorityYellow = view.findViewById(R.id.priority_btn_yellow);
        priorityYellow.setOnClickListener(this);
        priorityGreen = view.findViewById(R.id.priority_btn_green);
        priorityGreen.setOnClickListener(this);
        priorityRedSelected = view.findViewById(R.id.priority_btn_red_selected);
        priorityYellowSelected = view.findViewById(R.id.priority_btn_yellow_selected);
        priorityGreenSelected = view.findViewById(R.id.priority_btn_green_selected);
        cameraButton = view.findViewById(R.id.button_take_photo);

        actionPlanEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Required method deliberately left empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Required method left empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                actionPlan = actionPlanEditText.getText().toString();
            }
        });

        if (savedInstanceState != null) {
            description = savedInstanceState.getString("description");
            descriptionTextView.setText(description);
            options = savedInstanceState.getStringArrayList("options");
            rating = savedInstanceState.getInt("rating");

            String priorityStr = savedInstanceState.getString("priority");
            priority = Priority.valueOf(priorityStr);
            actionPlan = savedInstanceState.getString("actionPlan");
            actionPlanEditText.setText(actionPlan);
            photoPath = savedInstanceState.getString("photoPath");
        } else {
            descriptionTextView.setText(survey.getDescription());
            options = survey.getOptions();
            priority = survey.getPriority();
        }

        populateOptionRadioButtons(view);
        loadPriority();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraButton.setOnClickListener(this);
        } else {
            cameraButton.setEnabled(false);
        }

        if (photoPath != null && !photoPath.equals("")) {

        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "Activity created!");
        if (savedInstanceState != null) {
            Log.d(TAG, "We have state!!");
        } else {
            Log.d(TAG, "No state :(");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        actionPlan = actionPlanEditText.getText().toString();

        Log.d(TAG, "actionPlan: " + actionPlan);
        Log.d(TAG, "priority: " + priority);

        savedInstanceState.putString("description", description);
        savedInstanceState.putStringArrayList("options", options);
        savedInstanceState.putInt("rating", rating);

        if (priority != null) {
            savedInstanceState.putString("priority", priority.toString());
        }
        savedInstanceState.putString("actionPlan", actionPlan);
        savedInstanceState.putString("photoPath", photoPath);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.d(TAG, "in onViewStateRestored");

        if (savedInstanceState != null) {
            priority = Priority.valueOf(savedInstanceState.getString("priority"));
            loadPriority();
            actionPlan = savedInstanceState.getString("actionPlan");
            Log.d(TAG, "Loaded action plan!!");
            actionPlanEditText.setText(actionPlan);
        } else {
            Log.d(TAG, "Whatya know? No saved state");
        }

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
            int targetWidth = cameraButton.getWidth();
            int targetHeight = cameraButton.getHeight();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, bmOptions);
            int photoWidth = bmOptions.outWidth;
            int photoHeight = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoWidth/targetWidth, photoHeight/targetHeight);

            // Decode the image file into a Bitmap sized to fit the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap scaledBitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

            // Rotate the image
            /* TODO There must be a way to determine IF we need to rotate the image or not
               and/or manipulate the camera setup to make it output the correct orientation,
               but this is what works for now. */
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            ViewGroup.LayoutParams params = cameraButton.getLayoutParams();
            params.width = rotatedBitmap.getWidth();
            params.height = rotatedBitmap.getHeight();
            cameraButton.setLayoutParams(params);

            cameraButton.setImageBitmap(rotatedBitmap);
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
                if (options.get(i) != null && !options.get(i).equals("")) {
                    ((RadioButton) v).setText(options.get(i));
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + survey.getLocation() + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }

    public String getActionPlan() {
        return actionPlan;
    }
}

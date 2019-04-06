package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.model.Priority;
import com.plusmobileapps.safetyapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class WalkthroughContentFragment extends Fragment
        implements View.OnClickListener, WalkthroughFragmentContract.View {

    private enum Rating {
        OPTION1, OPTION2, OPTION3, OPTION4
    }

    static final String TAG = "WalkthruContentFragment";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Question walkthroughQuestion;
    private Response response = new Response();
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView schoolName;
    private TextView genLoc;
    private String description;
    private ImageButton cameraButton;
    private ArrayList<String> options = new ArrayList<>();
    private String rating;
    private Priority priority;
    private View priorityRed;
    private View priorityYellow;
    private View priorityGreen;
    private View saveButton;
    private View statusBar;
    private View editButton;
    private View mapPic;
    private View picDisplay;
    private TextView actionPlanLabel;
    private EditText actionPlanEditText;
    private String actionPlan;
    private String photoPath;
    private PackageManager packageManager;
    private WalkthroughFragmentContract.Presenter presenter;
    private RadioGroup radioGroup;
    private int currentRating = -1;
    private int currentPriority = -1;
    private static int count = 0;

    public static WalkthroughContentFragment newInstance(Question question, Response loadedResponse) {
        WalkthroughContentFragment fragment = new WalkthroughContentFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("walkthroughQuestion", new Gson().toJson(question));
        fragment.setArguments(bundle);

        if (loadedResponse == null) {
            Log.d(TAG, "No response loaded");
            fragment.response.setQuestionId(question.getQuestionId());
            fragment.response.setRating(-1);
            fragment.response.setPriority(-1);
            fragment.response.setActionPlan("");
        } else {
            fragment.response = loadedResponse;
            fragment.currentPriority = loadedResponse.getPriority();
            fragment.currentRating = loadedResponse.getRating();
            Log.d(TAG, "Loaded response:\n" + fragment.response.toString());
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        packageManager = this.getActivity().getPackageManager();
        View view;
        if(savedInstanceState.getString("priority") != null)
        {
            view = inflater.inflate(R.layout.activity_saved_action, container, false);
            String walkthroughJsonObject = getArguments().getString("walkthroughQuestion");


            walkthroughQuestion = new Gson().fromJson(walkthroughJsonObject, Question.class);
            initSavedViews(view);
            generateSavedView(view, walkthroughQuestion, savedInstanceState);
        }
        else
        {
            view = inflater.inflate(R.layout.activity_new_action, container, false);
            String walkthroughJsonObject = getArguments().getString("walkthroughQuestion");


            walkthroughQuestion = new Gson().fromJson(walkthroughJsonObject, Question.class);
            initViews(view);
            generateQuestionView(view, walkthroughQuestion);

            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                cameraButton.setOnClickListener(this);
            } else {
                cameraButton.setVisibility(View.GONE);
            }
        }

        return view;
    }

    private View generateQuestionView(View view, Question question) {
        radioGroup = view.findViewById(R.id.walkthroughStatus);
        radioGroup.setOnCheckedChangeListener(ratingChangeListener);
        radioGroup.addView(generateRadioButton(question.getRatingOption1(), Rating.OPTION1));
        radioGroup.addView(generateRadioButton(question.getRatingOption2(), Rating.OPTION2));
        if (question.getRatingOption3() != null && !question.getRatingOption3().equals("")) {
            radioGroup.addView(generateRadioButton(question.getRatingOption3(), Rating.OPTION3));
        }
        if (question.getRatingOption4() != null && !question.getRatingOption4().equals("")) {
            radioGroup.addView(generateRadioButton(question.getRatingOption4(), Rating.OPTION4));
        }

        descriptionTextView.setText(question.getQuestionText());

        return view;
    }

    private View generateSavedView(View view, Question question, Bundle savedInstanceState) {

        statusBar = view.findViewById(R.id.statusBar);

        String temp = savedInstanceState.getString("priority");

        if(temp.equalsIgnoreCase("HIGH"))
        {
            statusBar.setBackgroundColor(Color.parseColor("FFF44336"));
        }
        else if (temp.equalsIgnoreCase("MEDIUM"))
        {
            statusBar.setBackgroundColor(Color.parseColor("FFFFEB3B"));
        }
        else
        {
            statusBar.setBackgroundColor(Color.parseColor("FF4CAF50"));
        }

        String picPath = savedInstanceState.getString("photoPath");
        Drawable picture = Drawable.createFromPath(picPath);

        picDisplay.setBackground(picture);

        actionPlanLabel.setText(savedInstanceState.getString("actionPlan"));

        descriptionTextView.setText(savedInstanceState.getString("description"));
        dateTextView.setText(savedInstanceState.getString("date"));
        locationTextView.setText(savedInstanceState.getString("area"));
        genLoc.setText(savedInstanceState.getString("building"));
        schoolName.setText(savedInstanceState.getString("campus"));
        String tester = schoolName.getText().toString();
        if(tester.contains("Arizona State University") || tester.contains("ASU"))
        {
            mapPic.setBackgroundResource(R.drawable.ASU_map);
        }

        return view;
    }

    private RadioButton generateRadioButton(String content, Rating id) {
        RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null);
        radioButton.setId(id.ordinal());
        radioButton.setText(content);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        radioButton.setLayoutParams(layoutParams);
        return radioButton;
    }

    private void initViews(View view) {
        descriptionTextView = view.findViewById(R.id.fieldDesc);
        actionPlanLabel = view.findViewById(R.id.fieldTitle);
        actionPlanEditText = view.findViewById(R.id.fieldTitle);
        locationTextView = view.findViewById(R.id.fieldArea);
        genLoc = view.findViewById(R.id.fieldBuilding);
        schoolName = view.findViewById(R.id.fieldCampus);
        priorityRed = view.findViewById(R.id.highPriorityButton);
        priorityRed.setOnClickListener(this);
        priorityYellow = view.findViewById(R.id.mediumPriorityButton);
        priorityYellow.setOnClickListener(this);
        priorityGreen = view.findViewById(R.id.lowPriorityButton);
        priorityGreen.setOnClickListener(this);
        cameraButton = view.findViewById(R.id.picButton);
        cameraButton.setOnClickListener(this);
        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
    }

    private void initSavedViews(View view) {
        descriptionTextView = view.findViewById(R.id.finishedDescText);
        actionPlanLabel = view.findViewById(R.id.finishedTitleText);
        dateTextView = view.findViewById(R.id.actionDateText);
        locationTextView = view.findViewById(R.id.actionLocationText);
        schoolName = view.findViewById(R.id.schoolNameText);
        genLoc = view.findViewById(R.id.genLocationText);
        mapPic = view.findViewById(R.id.schoolMap);
        picDisplay = view.findViewById(R.id.picPlaceholder);
        editButton = view.findViewById(R.id.editSavedWalkthroughButton);
        editButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        actionPlan = actionPlanEditText.getText().toString();
        savedInstanceState.putString("description", description);
        savedInstanceState.putStringArrayList("options", options);
        savedInstanceState.putInt("rating", currentRating);
        savedInstanceState.putString("area", locationTextView.getText().toString());
        savedInstanceState.putString("building", genLoc.getText().toString());
        savedInstanceState.putString("campus", schoolName.getText().toString());

        savedInstanceState.putString("date", new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

        if (priority != null) {
            savedInstanceState.putString("priority", priority.toString());
        }
        savedInstanceState.putString("actionPlan", actionPlan);
        savedInstanceState.putString("photoPath", photoPath);
    }


    @Override
    public void setPresenter(WalkthroughFragmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRating(int rating) {
        radioGroup.check(rating);
    }

    @Override
    public void showPriority(Priority priority) {
        //TODO Saved action plan settings
    }

    /*
    @Override
    public void showPriority(Priority priority) {
        switch (priority) {
            case HIGH:
                priorityRed.setVisibility(View.GONE);
                priorityRedSelected.setVisibility(View.VISIBLE);
                priorityYellow.setVisibility(View.VISIBLE);
                priorityYellowSelected.setVisibility(View.GONE);
                priorityGreen.setVisibility(View.VISIBLE);
                priorityGreenSelected.setVisibility(View.GONE);
                break;
            case MEDIUM:
                priorityYellow.setVisibility(View.GONE);
                priorityYellowSelected.setVisibility(View.VISIBLE);
                priorityRed.setVisibility(View.VISIBLE);
                priorityRedSelected.setVisibility(View.GONE);
                priorityGreen.setVisibility(View.VISIBLE);
                priorityGreenSelected.setVisibility(View.GONE);
                break;
            case NONE:
                priorityGreen.setVisibility(View.GONE);
                priorityGreenSelected.setVisibility(View.VISIBLE);
                priorityYellow.setVisibility(View.VISIBLE);
                priorityYellowSelected.setVisibility(View.GONE);
                priorityRed.setVisibility(View.VISIBLE);
                priorityRedSelected.setVisibility(View.GONE);
                break;
            default:
                Log.d(TAG, "WalkthroughQuestion has unrecognized priority!");
                break;
        }
    }
    */

    @Override
    public void showActionPlan(String actionPlan) {
        actionPlanEditText.setText(actionPlan);
    }

    @Override
    public void showPhoto(String imagePath) {
        if (imagePath != null) {
            File file = new File(imagePath);
            Picasso.get().load(file).into(cameraButton);
        }
        // Can add this photo rotation and resizing back in if needed.
        /*int targetWidth = cameraButton.getWidth();
        int targetHeight = cameraButton.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);

        // Decode the image file into a Bitmap sized to fit the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap scaledBitmap = BitmapFactory.decodeFile(imagePath, bmOptions);

        // Rotate the image
        *//* TODO There must be a way to determine IF we need to rotate the image or not
           and/or manipulate the camera setup to make it output the correct orientation,
           but this is what works for now.*//*
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        ViewGroup.LayoutParams params = cameraButton.getLayoutParams();
        params.width = rotatedBitmap.getWidth();
        params.height = rotatedBitmap.getHeight();
        cameraButton.setLayoutParams(params);

        cameraButton.setImageBitmap(rotatedBitmap);*/
    }

    @Override
    public void enableActionPlan(boolean show) {
        actionPlanLabel.setEnabled(show);
        actionPlanEditText.setEnabled(show);
    }

    @Override
    public Response getResponse() {
        response.setActionPlan(actionPlanEditText.getText().toString());
        response.setRating(currentRating);
        response.setPriority(currentPriority);
        return response;
    }

    @Override
    public Response getLoadedResponse() {
        return response;
    }

    private RadioGroup.OnCheckedChangeListener ratingChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            response.setIsPersisted(true);

            switch (checkedId) {
                case 0:
                    currentRating = Rating.OPTION1.ordinal();
                    break;
                case 1:
                    currentRating = Rating.OPTION2.ordinal();
                    break;
                case 2:
                    currentRating = Rating.OPTION3.ordinal();
                    break;
                case 3:
                    currentRating = Rating.OPTION4.ordinal();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Handle all click events here within the fragment
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.picButton:
                Log.d(TAG, "Taking a photo...");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File imageFile = null;

                    try {
                        imageFile = createImageFile();
                        photoPath = imageFile.getAbsolutePath();
                        Log.d(TAG, "photoPath: " + photoPath);
                        response.setImagePath(photoPath);
                    } catch (IOException ioe) {
                        Log.d(TAG, "Error while creating image file");
                        ioe.printStackTrace();
                    }

                    if (imageFile != null) {
                        Uri imageURI = FileProvider.getUriForFile(this.getActivity(),
                                "com.plusmobileapps.safetyapp.fileprovider",
                                imageFile);
                        Log.d(TAG, "imageURI: " + imageURI.toString());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                break;
            case R.id.highPriorityButton:
                priority = Priority.HIGH;
                presenter.priorityClicked(priority);
                currentPriority = Priority.HIGH.ordinal();
                response.setIsActionItem(1);
                break;
            case R.id.mediumPriorityButton:
                priority = Priority.MEDIUM;
                presenter.priorityClicked(priority);
                currentPriority = Priority.MEDIUM.ordinal();
                response.setIsActionItem(1);
                break;
            case R.id.lowPriorityButton:
                priority = Priority.NONE;
                presenter.priorityClicked(priority);
                currentPriority = Priority.NONE.ordinal();
                break;
            case R.id.saveButton:
                //TODO Save things here
                break;
            case R.id.editSavedWalkthroughButton:
                //TODO Edit things here
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            presenter.photoTaken(photoPath);
            response.setIsPersisted(true);
        }
    }

    private File createImageFile() throws IOException
    {
        String imageFileName = "JPEG_" + count;
        count++;
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        /*photoPath = image.getAbsolutePath();
        Log.d(TAG, "photoPath: " + photoPath);
        response.setImagePath(photoPath);*/
        return image;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
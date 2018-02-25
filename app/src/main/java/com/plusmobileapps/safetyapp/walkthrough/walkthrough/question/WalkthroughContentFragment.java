package com.plusmobileapps.safetyapp.walkthrough.walkthrough.question;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.model.Priority;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.WalkthroughContract;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.WalkthroughPresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class WalkthroughContentFragment extends Fragment implements View.OnClickListener, WalkthroughFragmentContract.View {

    static final String TAG = "WalkthruContentFragment";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Question walkthroughQuestion;
    private Response response;
    private TextView descriptionTextView;
    private String description;
    private ImageButton cameraButton;
    private ArrayList<String> options = new ArrayList<>();
    private String rating;
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
    private WalkthroughFragmentContract.Presenter presenter;

    public static WalkthroughContentFragment newInstance(Question question) {
        WalkthroughContentFragment fragment = new WalkthroughContentFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("walkthroughQuestion", new Gson().toJson(question));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        packageManager = this.getActivity().getPackageManager();
        View view = inflater.inflate(R.layout.fragment_walkthrough_question, container, false);
        String walkthroughJsonObject = getArguments().getString("walkthroughQuestion");

        walkthroughQuestion = new Gson().fromJson(walkthroughJsonObject, Question.class);
        initViews(view);
        generateQuestionView(view, walkthroughQuestion);

        loadPriority();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraButton.setOnClickListener(this);
        } else {
            cameraButton.setVisibility(View.GONE);
        }

        return view;
    }

    private View generateQuestionView(View view, Question question) {
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        if (question.getRatingOption1() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption1(), question.getQuestionId()));
        }
        if(question.getRatingOption2() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption2(), question.getQuestionId()));
        }
        if(question.getRatingOption3() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption3(), question.getQuestionId()));
        }
        if(question.getRatingOption4() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption4(), question.getQuestionId()));
        }

        descriptionTextView.setText(question.getQuestionText());

        return view;
    }

    private RadioButton generateRadioButton(String content, int id) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setId(id);
        radioButton.setText(content);
        return radioButton;
    }

    private void initViews(View view) {
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
        savedInstanceState.putString("rating", rating);

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
                walkthroughQuestion.setActionItem(true);
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
                walkthroughQuestion.setActionItem(true);
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
                walkthroughQuestion.setActionItem(false);
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



        walkthroughQuestion.setPriority(priority);
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
                    Log.d(TAG, "WalkthroughQuestion has unrecognized priority!");
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + walkthroughQuestion.getLocation() + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * handle the click events of the radio button group
     *
     * @param view  radiobutton from the walkthroughQuestion
     */
    public void onRadioButtonClicked(View view) {
        String rating = ((RadioButton) view).getText().toString();

        walkthroughQuestion.setRatingOption1(rating);

        Log.d(TAG, "New rating: " + walkthroughQuestion.getRating());
    }
}

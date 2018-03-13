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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private TextView titleRating;
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
    private TextView titlePriority;
    private String actionPlan;
    private String photoPath;
    private PackageManager packageManager;
    private WalkthroughFragmentContract.Presenter presenter;
    private RadioGroup radioGroup;
    private int currentRating = -1;

    public static WalkthroughContentFragment newInstance(Question question) {
        WalkthroughContentFragment fragment = new WalkthroughContentFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("walkthroughQuestion", new Gson().toJson(question));
        fragment.setArguments(bundle);
        fragment.response.setQuestionId(question.getQuestionId());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        packageManager = this.getActivity().getPackageManager();
        View view = inflater.inflate(R.layout.fragment_walkthrough_question, container, false);
        String walkthroughJsonObject = getArguments().getString("walkthroughQuestion");

        //TODO: Refactor response to init with foreign keys.
        //TODO: Revisit User table in the DB because we don't really need it.
        //Should create a response with foreign keys.
        response.setUserId(1);
        walkthroughQuestion = new Gson().fromJson(walkthroughJsonObject, Question.class);
        initViews(view);
        generateQuestionView(view, walkthroughQuestion);

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraButton.setOnClickListener(this);
        } else {
            cameraButton.setVisibility(View.GONE);
        }

        return view;
    }

    private View generateQuestionView(View view, Question question) {
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(ratingChangeListener);
        if (question.getRatingOption1() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption1(), Rating.OPTION1));
        }
        if (question.getRatingOption2() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption2(), Rating.OPTION2));
        }
        if (question.getRatingOption3() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption3(), Rating.OPTION3));
        }
        if (question.getRatingOption4() != null) {
            radioGroup.addView(generateRadioButton(question.getRatingOption4(), Rating.OPTION4));
        }

        titleRating.setText(question.getQuestionText());

        return view;
    }

    private RadioButton generateRadioButton(String content, Rating id) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setId(id.ordinal());
        radioButton.setText(content);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        radioButton.setLayoutParams(layoutParams);
        return radioButton;
    }

    private void initViews(View view) {
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
        titlePriority = view.findViewById(R.id.title_priority);
        titleRating = view.findViewById(R.id.title_rating);

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

    @Override
    public void enableActionPlan(boolean show) {
        actionPlanLabel.setEnabled(show);
        actionPlanEditText.setEnabled(show);
    }

    @Override
    public void showError(boolean showPriority, boolean showRating) {
        titleRating.setTextColor(getTextColor(showRating));
        titlePriority.setTextColor(getTextColor(showPriority));
    }

    private int getTextColor(boolean show) {
        return show ?
                getResources().getColor(R.color.actionItemRed) :
                getResources().getColor(R.color.textColorPrimary);
    }

    @Override
    public Response getResponse() {
        response.setActionPlan(actionPlanEditText.getText().toString());
        response.setRating(currentRating);
        return response;
    }

    private RadioGroup.OnCheckedChangeListener ratingChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
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
                presenter.priorityClicked(priority);
                response.setPriority(Priority.HIGH.ordinal());
                break;
            case R.id.priority_btn_yellow:
                priority = Priority.MEDIUM;
                presenter.priorityClicked(priority);
                response.setPriority(Priority.MEDIUM.ordinal());
                break;
            case R.id.priority_btn_green:
                priority = Priority.NONE;
                presenter.priorityClicked(priority);
                response.setPriority(Priority.NONE.ordinal());
                break;
            default:
                break;
        }
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
            int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);

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

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + walkthroughQuestion.getRatingOption4() + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        response.setImage(photoPath);
        return image;
    }

}

package com.plusmobileapps.safetyapp.actionitems.detail;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Response;

/**
 * Created by rbeerma
 * This activity displays the details of a specific action item.
 */
public class ActionItemDetailActivity extends AppCompatActivity
        implements EditPriorityDialogFragment.PriorityDialogListener,
                    ActionItemDetailContract.View {

    public static final String EXTRA_ACTION_ITEM_ID = "ACTION_ITEM_ID";

    private ActionItemDetailPresenter presenter;

    private Button editPriorityBtn;
    private View statusDot;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_item_detail);

        presenter = new ActionItemDetailPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_item_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_action_item_detail);

        statusDot = findViewById(R.id.action_item_status);
        editPriorityBtn = findViewById(R.id.edit_priority_btn);
        editPriorityBtn.setOnClickListener(editPriorityClickListener);
        saveButton = findViewById(R.id.save_action_item_detail);
        saveButton.setOnClickListener(saveListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onItemSelected(DialogFragment dialog, CharSequence selectedItem) {
        EditPriorityDialogFragment priorityDialog = (EditPriorityDialogFragment) dialog;
        String selectedPriority = priorityDialog.getSelectedItem().toString();

        switch (selectedPriority) {
            case "High":
                statusDot.setBackgroundResource(R.drawable.circle_red);
                break;
            case "Medium":
                statusDot.setBackgroundResource(R.drawable.circle_yellow);
                break;
            case "Low":
                statusDot.setBackgroundResource(R.drawable.circle_green);
                break;
            default:
                break;
        }
    }

    @Override
    public void showActionItem(Response response) {

    }

    @Override
    public void showPriorityDialog() {
        DialogFragment editPriorityDialog = new EditPriorityDialogFragment();
        editPriorityDialog.show(getSupportFragmentManager(), "EditPriorityDialogFragment");
    }

    @Override
    public Response getActionItemDetails() {
        return null;
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: save the data for any changes made to the action item
            finish(); //destroy the activity for prototype sake
        }
    };

    private View.OnClickListener editPriorityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.editPriorityButtonClicked();
        }
    };
}

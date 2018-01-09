package com.plusmobileapps.safetyapp.actionitems;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.plusmobileapps.safetyapp.R;

/**
 * Created by rbeerma on 12/28/2017.
 * This fragment displays a list dialog when the user clicks the "Edit Priority" button.
 */

public class EditPriorityDialogFragment extends DialogFragment {
    PriorityDialogListener priorityDialogListener;
    private final CharSequence[] PRIORITIES = {"High", "Medium", "Low"};

    public CharSequence getSelectedItem() {
        return selectedItem;
    }

    private CharSequence selectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_edit_priority_dialog)
                .setItems(PRIORITIES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        selectedItem = PRIORITIES[item];
                        priorityDialogListener.onItemSelected(EditPriorityDialogFragment.this, selectedItem);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            priorityDialogListener = (PriorityDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the PriorityDialogListener interface
            throw new ClassCastException(context.toString()
                    + "must implement PriorityDialogListener");
        }
    }

    public interface PriorityDialogListener {
        void onItemSelected(DialogFragment dialog, CharSequence selectedItem);
    }

}

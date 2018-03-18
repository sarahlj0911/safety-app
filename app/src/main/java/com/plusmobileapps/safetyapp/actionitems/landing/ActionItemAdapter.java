package com.plusmobileapps.safetyapp.actionitems.landing;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ViewHolder> {

    private static final String TAG = "ActionItemAdapter";
    private ActionItemsFragment.ActionItemListener itemListener;

    private List<Response> actionItems;

    public ActionItemAdapter(List<Response> actionItems, ActionItemsFragment.ActionItemListener itemListener){
        this.actionItems = actionItems;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_action_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        Response actionItem = actionItems.get(position);

        holder.getDescription().setText(actionItem.getActionPlan());
        holder.getLocation().setText(actionItem.getLocationName());
        holder.getTitle().setText(actionItem.getTitle());
//        holder.getPicture().setImageBitmap(actionItem.getPhoto());


    }

    @Override
    public int getItemCount() {
        return actionItems.size();
    }

    public void replaceData(List<Response> actionItems) {
        setList(actionItems);
        notifyDataSetChanged();
    }

    public void dismissActionItem(int position) {
        actionItems.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreActionItem(int position, Response response) {
        actionItems.add(position, response);
        notifyItemInserted(position);
    }

    private void setList(List<Response> actionItems) {
        this.actionItems = actionItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View status = itemView.findViewById(R.id.action_item_status);
        private final TextView title = itemView.findViewById(R.id.action_item_title);
        private final TextView description = itemView.findViewById(R.id.action_item_description);

        private final TextView location = itemView.findViewById(R.id.action_item_location);
        private final ImageButton dismissButton = itemView.findViewById(R.id.dismiss_action_item_button);
        //TODO: figure out how to add image to persistence

        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            dismissButton.setOnClickListener(dismissListener);
        }

        @Override
        public void onClick(View v) {
            itemListener.onActionItemClicked(getAdapterPosition());
        }

        private View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onDismissButtonClicked(getAdapterPosition());
            }
        };

        public View getStatus() {
            return status;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getLocation() {
            return location;
        }

        public TextView getDescription() {
            return description;
        }

    }


}

package com.plusmobileapps.safetyapp.actionitems.landing;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ViewHolder> {

    private static final String TAG = "ActionItemAdapter";
    private Response actionItem;
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
        actionItem = actionItems.get(position);

        holder.getDescription().setText(actionItem.getActionPlan());
        holder.getLocation().setText(actionItem.getLocationId());
//        holder.getPicture().setImageBitmap(actionItem.getPhoto());

        //TODO handle the changing of statuses and bind the corresponding colors to status value
    }

    @Override
    public int getItemCount() {
        return actionItems.size();
    }

    public void replaceData(List<Response> actionItems) {
        setList(actionItems);
        notifyDataSetChanged();
    }

    private void setList(List<Response> actionItems) {
        this.actionItems = actionItems;
    }

    public Response getActionItem(int position) {
        return actionItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View status = itemView.findViewById(R.id.action_item_status);
        private final TextView title = itemView.findViewById(R.id.action_item_title);
        private final TextView description = itemView.findViewById(R.id.action_item_description);

        private final TextView location = itemView.findViewById(R.id.action_item_location);
        //TODO: figure out how to add image to persistence

        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Response actionItem = getActionItem(getAdapterPosition());
            itemListener.onActionItemClicked(actionItem);
        }

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

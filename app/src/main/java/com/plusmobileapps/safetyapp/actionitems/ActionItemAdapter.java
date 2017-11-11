package com.plusmobileapps.safetyapp.actionitems;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.plusmobileapps.safetyapp.R;


/**
 * Created by Andrew on 11/8/2017.
 */

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ViewHolder> {

    private static final String TAG = "ActionItemAdapter";
    private ActionItem actionItem;

    private ArrayList<ActionItem> actionItems;

    public ActionItemAdapter(ArrayList<ActionItem> actionItems){
        this.actionItems = actionItems;
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

        holder.getDescription().setText(actionItem.getDescription());
        holder.getLocation().setText(actionItem.getLocation());
        holder.getPicture().setImageBitmap(actionItem.getPhoto());
        holder.getTitle().setText(actionItem.getTitle());

        //TODO handle the changing of statuses and bind the corresponding colors to status value
    }

    @Override
    public int getItemCount() {
        return actionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO
        private final Context context;
        private final View status = itemView.findViewById(R.id.action_item_status);
        private final TextView title = itemView.findViewById(R.id.action_item_title);
        private final TextView location = itemView.findViewById(R.id.action_item_location);
        private final TextView description = itemView.findViewById(R.id.action_item_description);
        private final ImageView picture = itemView.findViewById(R.id.action_item_image);

        public ViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //handle the click of the view holder here
                }
            });
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

        public ImageView getPicture() {
            return picture;
        }
    }


}

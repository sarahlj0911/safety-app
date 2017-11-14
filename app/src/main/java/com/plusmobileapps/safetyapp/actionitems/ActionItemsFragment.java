package com.plusmobileapps.safetyapp.actionitems;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plusmobileapps.safetyapp.R;

import java.util.ArrayList;


public class ActionItemsFragment extends Fragment {
    private static final String TAG = "ActionItemsFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected ActionItemsFragment.LayoutManagerType currentLayoutManagerType;
    protected ActionItemAdapter adapter;
    private ArrayList<ActionItem> actionItems;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    public ActionItemsFragment() {
        // Required empty public constructor
    }

    public static ActionItemsFragment newInstance() {
        ActionItemsFragment fragment = new ActionItemsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_action_items, container, false);
        rootView.setTag(TAG);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.action_items_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        recyclerView.setLayoutManager(layoutManager);

        actionItems = new ArrayList<>();
        populateActionItems();
        adapter = new ActionItemAdapter(actionItems);

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void populateActionItems(){
        ActionItem actionItem = new ActionItem("Graffiti", "Boys Bathroom", 1, "The boys got a hold of some sharpies and really did a number on the bathroom stall. There was some profanity written that needs to be removed immediately. I don't know what kind of monster would write such a thing.");

        for (int i = 0; i < 20; i++){
            actionItems.add(actionItem);
        }
    }
}

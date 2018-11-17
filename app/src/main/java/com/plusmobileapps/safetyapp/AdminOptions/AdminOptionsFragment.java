package com.plusmobileapps.safetyapp.AdminOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.actionitems.detail.ActionItemDetailActivity;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemAdapter;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemContract;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemsFragment;
import com.plusmobileapps.safetyapp.data.entity.Response;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


public class AdminOptionsFragment extends Fragment implements ActionItemContract {
    private static final String TAG = "FragmentAdminOptions";
    protected RecyclerView recyclerView;
    protected ActionItemAdapter adapter;
    private ActionItemContract.Presenter presenter;


    public AdminOptionsFragment(){
        //constructor
    }
    public static AdminOptionsFragment newInstance() {
        return new AdminOptionsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        android.view.View rootView = inflater.inflate(R.layout.activity_admin_options, container, false);
        rootView.setTag(TAG);

        adapter = new ActionItemAdapter(new ArrayList<Response>(0), itemListener);

        recyclerView = rootView.findViewById(R.id.action_items_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new SlideInRightAnimator());

        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }



        ActionItemsFragment.ActionItemListener itemListener = new ActionItemsFragment.ActionItemListener() {
            @Override
            public void onActionItemClicked(int position) {
                presenter.openActionItemDetail(position);
            }

            @Override
            public void onDismissButtonClicked(int position) {
                presenter.dismissButtonClicked(position);
            }
        };

        /**
         * Interface for the recyclerview items being clicked
         */
        public interface ActionItemListener {
            void onActionItemClicked(int position);

            void onDismissButtonClicked(int position);
        }
    }














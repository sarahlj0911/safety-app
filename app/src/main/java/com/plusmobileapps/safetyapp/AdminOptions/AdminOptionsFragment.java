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




}

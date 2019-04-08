package com.plusmobileapps.safetyapp.actionitems.landing;

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
import com.plusmobileapps.safetyapp.data.entity.Response;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


public class ActionItemsFragment extends Fragment implements ActionItemContract.View {

    private static final String TAG = "ActionItemsFragment";
    protected RecyclerView recyclerView;
    protected ActionItemAdapter adapter;
    private ActionItemContract.Presenter presenter;

    public ActionItemsFragment() {
        // Required empty public constructor
    }

    public static ActionItemsFragment newInstance() {
        return new ActionItemsFragment();
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

        adapter = new ActionItemAdapter(new ArrayList<Response>(0), itemListener);

        recyclerView = rootView.findViewById(R.id.action_items_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //seting view for action items
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new SlideInRightAnimator());

        return rootView;
    }

    @Override
    public void setPresenter(ActionItemContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    /**
     * method called from the presenter to load all the action items data
     *
     * @param actionItems list of all action items to pass to adapter
     */
    @Override
    public void showActionItems(List<Response> actionItems) {
        adapter.replaceData(actionItems);

    }

    @Override
    public void showNoActionItems(boolean show) {
        TextView noActionItemText = getView().findViewById(R.id.no_action_items);
        recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
        if (noActionItemText != null) {
            noActionItemText.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * launch the intent for the ActionItemDetailActivity
     *
     * @param actionItemId primary key for the ActionItem
     */
    @Override
    public void showActionItemDetailUi(String actionItemId) {
        Intent intent = new Intent(getContext(), ActionItemDetailActivity.class);
        intent.putExtra(ActionItemDetailActivity.EXTRA_ACTION_ITEM_ID, actionItemId);
        startActivity(intent);
    }

    @Override
    public void dismissActionItem(int position) {
        adapter.dismissActionItem(position);
        CoordinatorLayout coordinatorLayout = getView().findViewById(R.id.action_item_coordinator);
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.dismiss_action_item), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action_undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.undoDismissal();
                        }
                    });

            snackbar.show();
        }
    }

    @Override
    public void restoreActionItem(int position, Response response) {
        adapter.restoreActionItem(position, response);
        recyclerView.smoothScrollToPosition(position);
    }

    /**
     * called from the presenter to toggle the loading state of the view
     *
     * @param active boolean to toggle progress indicator
     */
    @Override
    public void setProgressIndicator(boolean active) {
        //TODO toggle the progress indicator on and off here
        //progressIndicator.setEnable(active);
    }

    /**
     * Listener for clicks of action items in the recyclerview
     */
    ActionItemListener itemListener = new ActionItemListener() {
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

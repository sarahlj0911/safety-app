package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.walkthrough.location.LocationActivity;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.ArrayList;
import java.util.List;

public class WalkthroughLandingFragment extends Fragment
        implements OnShowcaseEventListener, WalkthroughLandingContract.View {

    public static String EXTRA_REQUESTED_WALKTHROUGH = "requested_walkthrough";
    public static String EXTRA_WALKTHROUGH_NAME = "walkthrough_name";

    private static ShowcaseView showcaseView;
    private static final String TAG = "WalkthruLandingFragment";
    private PrefManager prefManager;
    private View overlay;
    private View noWalkthroughs;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private WalkthroughLandingAdapter adapter;

    private WalkthroughLandingContract.Presenter presenter;

    public WalkthroughLandingFragment() {
        // Required empty public constructor
    }


    public static WalkthroughLandingFragment newInstance() {
        WalkthroughLandingFragment fragment = new WalkthroughLandingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_walkthrough_landing, container, false);
        rootView.setTag(TAG);
        recyclerView = rootView.findViewById(R.id.landing_walkthrough_recyclerview);
        overlay = rootView.findViewById(R.id.overlay);
        noWalkthroughs = rootView.findViewById(R.id.no_walkthrough);
        fab = rootView.findViewById(R.id.floatingActionButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WalkthroughLandingAdapter(new ArrayList<Walkthrough>(0), itemListener);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(fabListener);

        return rootView;
    }

    @Override
    public void setPresenter(WalkthroughLandingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        prefManager = new PrefManager(getContext());

        if (!prefManager.getHasSeenCreateWalkthroughTutorial()) {
            presenter.firstAppLaunch();
        }

        // presenter has to be started in either case
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void showWalkthroughs(List<Walkthrough> walkthroughs) {
        Log.d(TAG, "In showWalkthroughs. walkthroughs.size = " + walkthroughs.size());
        showNoWalkthroughs(walkthroughs.size() == 0);
        fab.setVisibility(View.VISIBLE);
        adapter.replaceData(walkthroughs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openWalkthrough(long id, String title) {
        fab.setVisibility(View.GONE);
        Intent intent = new Intent(getContext(), LocationActivity.class);
        intent.putExtra(EXTRA_REQUESTED_WALKTHROUGH, id);
        intent.putExtra(EXTRA_WALKTHROUGH_NAME, title);
        startActivity(intent);
    }

    @Override
    public void createNewWalkthrough(String title) {
        Intent intent = new Intent(getContext(), LocationActivity.class);
        intent.putExtra(EXTRA_WALKTHROUGH_NAME, title);
        startActivity(intent);
    }

    @Override
    public void showTutorial() {
        fab.setClickable(false);
        overlay.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        int margin = ((Number) (getResources().getDisplayMetrics().density *12)).intValue();
        params.setMargins(margin, margin, margin, margin);

        ViewTarget target = new ViewTarget(R.id.floatingActionButton, getActivity());
        showcaseView = new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setTarget(target)
                .setContentTitle(R.string.tutorial_title)
                .setContentText(R.string.tutorial_content)
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShowcaseEventListener(this)
                .replaceEndButton(R.layout.tutorial_custom_button)
                .hideOnTouchOutside()
                .setOnClickListener(tutorialClickListener)
                .build();
        showcaseView.setButtonPosition(params);
    }

    @Override
    public void showInProcessConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.walkthrough_in_progress_dialog_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteInProgressWalkthroughConfirmed();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showCreateWalkthroughDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.tutorial_title))
                .setView(getLayoutInflater().inflate(R.layout.dialog_create_walkthrough, null))
                .setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog dialogObj = Dialog.class.cast(dialog);
                        EditText editText = dialogObj.findViewById(R.id.edittext_create_walkthrough);
                        String walkthroughTitle = editText.getText().toString();
                        presenter.confirmCreateWalkthroughClicked(walkthroughTitle);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user clicked cancel
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showNoWalkthroughs(boolean show) {
        if(show) {
            noWalkthroughs.setVisibility(View.VISIBLE);
        } else {
            noWalkthroughs.setVisibility(View.GONE);
        }
    }

    /**
     * handle click listeners
     */
    private View.OnClickListener tutorialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //prefManager.setIsTutorialSeen(false);
            prefManager.setUserSeenCreateWalkthroughTutorial(true);
            showcaseView.hide();
        }
    };

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.createNewWalkthroughIconClicked();
        }
    };

    /**
     * Handle Recyclerview clicks
     */
    private WalkthroughLandingItemListener itemListener = new WalkthroughLandingItemListener() {
        @Override
        public void onWalkthroughClicked(int position) {
            presenter.walkthroughClicked(position);
        }
    };

    public interface WalkthroughLandingItemListener {
        void onWalkthroughClicked(int position);
    }

    /**
     * Callbacks for the showcase view
     */
    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        fab.setClickable(true);
        overlay.setVisibility(View.GONE);
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {
    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

}

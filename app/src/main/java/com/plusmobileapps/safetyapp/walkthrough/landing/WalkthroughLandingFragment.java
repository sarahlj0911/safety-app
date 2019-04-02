package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.sync.DownloadCallback;
import com.plusmobileapps.safetyapp.sync.NetworkChangeReceiver;
import com.plusmobileapps.safetyapp.sync.NetworkFragment;
import com.plusmobileapps.safetyapp.util.NetworkUtil;
import com.plusmobileapps.safetyapp.walkthrough.location.LocationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalkthroughLandingFragment extends Fragment
        implements OnShowcaseEventListener, WalkthroughLandingContract.View, DownloadCallback {

    public static String EXTRA_WALKTHROUGH_NAME = "walkthrough_name";
    private static final int MINIMUM_CHARACTER_NAME = 2;

    private ShowcaseView showcaseView;
    private static final String TAG = "WalkthruLandingFragment";
    private PrefManager prefManager;
    private View overlay;
    private View noWalkthroughs;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private WalkthroughLandingAdapter adapter;
    private ProgressBar progressBar;
    private NetworkFragment networkFragment;
    private NetworkChangeReceiver networkChangeReceiver;
    boolean downloading;
    View rootView;

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
        networkFragment = NetworkFragment.getInstance(getFragmentManager());
        networkFragment.setCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_walkthrough_landing, container, false);
        rootView.setTag(TAG);
        networkChangeReceiver = NetworkUtil.registerNetworkListener(rootView.getContext(), rootView.findViewById(R.id.fragment_walkthrough_landing_root), this);

        recyclerView = rootView.findViewById(R.id.landing_walkthrough_recyclerview);
        overlay = rootView.findViewById(R.id.overlay);
        noWalkthroughs = rootView.findViewById(R.id.no_walkthrough);
        fab = rootView.findViewById(R.id.floatingActionButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WalkthroughLandingAdapter(new ArrayList<Walkthrough>(0), itemListener);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(fabListener);
        progressBar = rootView.findViewById(R.id.pb_loading_indicator);

        return rootView;
    }

    @Override
    public void setPresenter(WalkthroughLandingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        prefManager = new PrefManager(Objects.requireNonNull(getContext()));

        if (!prefManager.getHasSeenCreateWalkthroughTutorial()) {
            presenter.firstAppLaunch();
        }

        downloadData();

        Log.d(TAG, "Resumed; should start presenter and upload data");

        // presenter has to be started in either case
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NetworkUtil.unregisterNetworkListener(rootView.getContext(), networkChangeReceiver);
    }

    @Override
    public void showWalkthroughs(List<Walkthrough> walkthroughs) {
        Log.d(TAG, "In showWalkthroughs. walkthroughs.size = " + walkthroughs.size());
        //fab.setVisibility(View.VISIBLE); // TODO fix?
        fab.show();
        adapter.replaceData(walkthroughs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openWalkthrough(int id, String title) {
        //fab.setVisibility(View.GONE);
        fab.show();
        Intent intent = new Intent(getContext(), LocationActivity.class);
        intent.putExtra(LocationActivity.EXTRA_WALKTHROUGH_ID, id);
        intent.putExtra(EXTRA_WALKTHROUGH_NAME, title);
        startActivity(intent);
    }

    @Override
    public void createNewWalkthrough(int id, String title) {
        Walkthrough walkthrough = new Walkthrough(title);
        Intent intent = new Intent(getContext(), LocationActivity.class);
        intent.putExtra(EXTRA_WALKTHROUGH_NAME, title);
        intent.putExtra(LocationActivity.EXTRA_WALKTHROUGH_ID, walkthrough.getWalkthroughId());

        startActivity(intent);
    }

    @Override
    public void showTutorial() {
        fab.setClickable(false);
        overlay.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        params.setMargins(margin, margin, margin, margin);

        ViewTarget target = new ViewTarget(R.id.floatingActionButton, Objects.requireNonNull(getActivity()));
        showcaseView = new ShowcaseView.Builder(getActivity())
                .withMaterialShowcase()
                .setTarget(target)
                .setContentTitle(R.string.tutorial_title)
                .setContentText(R.string.tutorial_content)
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShowcaseEventListener(this)
                .replaceEndButton(R.layout.tutorial_custom_button)
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.tutorial_title))
                .setView(getLayoutInflater().inflate(R.layout.dialog_create_walkthrough, null))
                .setPositiveButton(getString(R.string.create), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                final Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialogObj = (Dialog) dialog;
                        TextInputLayout textInputLayout = dialogObj.findViewById(R.id.edit_text_create_walkthrough);
                        String walkthroughTitle = textInputLayout.getEditText().getText().toString();
                        if (walkthroughTitle != null & walkthroughTitle.length() >= MINIMUM_CHARACTER_NAME) {
                            presenter.confirmCreateWalkthroughClicked(walkthroughTitle);
                            dialog.dismiss();
                        } else {
                            textInputLayout.setError(getString(R.string.error_create_walkthrough_name));
                        }
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public void showNoWalkThrough(boolean show) {
        noWalkthroughs.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * handle click listeners
     */
    private View.OnClickListener tutorialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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

        @Override
        public void onDismissButtonClicked(final int position, final CheckBox dismissButton) {
            //TODO: Confirmation dialogue.
            //On okay: mark walkthrough as completed.
            //On cancel: un check box.
            final Walkthrough selectedWalkthrough = adapter.getWalkthroughs().get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Completing this walkthrough will compelete it for all users at your school. Are you sure?")
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.setWalkthroughCompleted(selectedWalkthrough, presenter);
                            dismissButton.setVisibility(View.INVISIBLE);
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissButton.setChecked(false);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    };

    public interface WalkthroughLandingItemListener {
        void onWalkthroughClicked(int position);

        void onDismissButtonClicked(int position, CheckBox dismissButton);
    }

    /**
     * Callbacks for the showcase view
     */
    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        fab.setClickable(true);
        overlay.setVisibility(View.GONE);
        prefManager.setUserSeenCreateWalkthroughTutorial(true);
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

    // Below functions are for downloading remote walkthrough/response data for the entered school
    private void downloadData() {
        if (!downloading && networkFragment != null) {
            networkFragment.startDownload();
            downloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {
        downloading = true;
        // TODO progress bar show when server is ready
        //showProgressBar(true);
        Log.d(TAG, "Result from DownloadTask: " + result);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            // You can add UI behavior for progress updates here.
            case DownloadCallback.Progress.ERROR:

                break;
            case DownloadCallback.Progress.CONNECT_SUCCESS:

                break;
            case DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        showProgressBar(false);

        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }
}

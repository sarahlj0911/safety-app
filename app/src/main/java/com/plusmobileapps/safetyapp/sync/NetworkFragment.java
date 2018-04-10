package com.plusmobileapps.safetyapp.sync;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

//import org.jetbrains.annotations.Nullable;

/**
 * Created by Robert Beerman on 4/5/2018.
 */

public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";

    //private static final String URL_KEY = "UrlKey";

    private DownloadCallback callback;
    private DownloadTask downloadTask;

    public static NetworkFragment getInstance(FragmentManager fragmentManager) {
        NetworkFragment networkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        //args.putString(URL_Key, url);
        networkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;

    }

    @Override
    /*public void onCreate(@Nullable Bundle savedInstanceState) {*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //urlString = getArguments().getString(URL_KEY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task
        callback = (DownloadCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak
        callback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed
        cancelDownload();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of DownloadTask
     */
    public void startDownload() {
        cancelDownload();
        downloadTask = new DownloadTask(callback);
        downloadTask.execute();
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution
     */
    public void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancel(true);
        }
    }

}

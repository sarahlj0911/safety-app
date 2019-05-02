package com.ASUPEACLab.safetyapp.sync;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Robert Beerman on 4/5/2018.
 */

public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    private DownloadCallback callback;
    private DownloadTask downloadTask;

    public static NetworkFragment getInstance(FragmentManager fragmentManager) {
        NetworkFragment networkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        networkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed
        cancelDownload();
        super.onDestroy();
        callback = null;
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

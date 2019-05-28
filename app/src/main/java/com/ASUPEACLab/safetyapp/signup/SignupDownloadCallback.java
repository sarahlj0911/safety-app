package com.ASUPEACLab.safetyapp.signup;

import android.net.NetworkInfo;

import java.util.ArrayList;

public interface SignupDownloadCallback {

    interface Progress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int PROCESS_INPUT_STREAM_SUCCESS = 3;
    }

    ArrayList<String> updateFromDownload(String result, ArrayList<String> schoolList);

    NetworkInfo getActiveNetworkInfo();

    void onProgressUpdate(int progressCode, int percentComplete);

    void finishDownloading();
}

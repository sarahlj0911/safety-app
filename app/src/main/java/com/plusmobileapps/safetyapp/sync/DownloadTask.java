package com.plusmobileapps.safetyapp.sync;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.net.ConnectivityManagerCompat;

import java.net.URL;


/**
 * Created by Robert Beerman on 4/5/2018.
 */

public class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {

    private static final String TAG = "DownloadTask";

    private DownloadCallback callback;

    DownloadTask(DownloadCallback callback) {
        setCallback(callback);
    }

    void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    static class Result {
        public String resultValue;
        public Exception exception;

        public Result(String resultValue) {
            resultValue = resultValue;
        }

        public Result(Exception exception) {
            this.exception = exception;
        }
    }

    /**
     * Cancel background network operation if we do not have network connectivity
     */
    @Override
    protected void onPreExecute() {
        if (callback != null) {
            NetworkInfo networkInfo = callback.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected()
                    || (networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data
                callback.updateFromDownload(null);
                //cancel(true);
            }
        }
    }

    /**
     * Defines work to perform on the background thread. This is where the magic happens!
     */
    @Override
    protected DownloadTask.Result doInBackground(String... urls) {
        Result result = null;
        //if (!isCancelled() && urls != null && urls.length > 0) {
            //String urlString = urls[0];
            try {
                //URL url = new URL(urlString);

            } catch(Exception e) {
                result = new Result(e);
            }

        //}

        return result;
    }

    /**
     * Updates the DownloadCallback with the result
     */
    @Override
    protected void onPostExecute(Result result) {
        if (result != null && callback != null) {
            callback.updateFromDownload(result.exception.getMessage());
        } else if (result.resultValue != null) {
            callback.updateFromDownload(result.resultValue);
        }

        callback.finishDownloading();
    }

    /**
     * Override to add special behavior for cancelled AsyncTask
     */
    @Override
    protected void onCancelled(Result result) {

    }
}

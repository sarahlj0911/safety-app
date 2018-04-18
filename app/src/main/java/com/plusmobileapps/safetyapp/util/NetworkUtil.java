package com.plusmobileapps.safetyapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.sync.NetworkChangeReceiver;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingContract;

import static com.plusmobileapps.safetyapp.sync.NetworkChangeReceiver.IS_NETWORK_AVAILABLE;

public class NetworkUtil {

    /**
     * Will show a snack bar whenever the network state changes on the device
     *
     * @param context   the fragment or activity context
     * @param view      the view on which to show the snack bar
     */
    /*public static void registerNetworkListener(@NonNull Context context, @NonNull View view) {*/
    public static NetworkChangeReceiver registerNetworkListener(@NonNull Context context, @NonNull View view, final WalkthroughLandingContract.View walkthroughLandingFragment) {
        final Snackbar snackbar = Snackbar.make(view, context.getString(R.string.network_unavailable), Snackbar.LENGTH_INDEFINITE);
        NetworkChangeReceiver receiver = new NetworkChangeReceiver();


        IntentFilter appIntentFilter = new IntentFilter();
        appIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        /*context.registerReceiver(new NetworkChangeReceiver(), appIntentFilter);*/
        context.registerReceiver(receiver, appIntentFilter);
        IntentFilter networkIntentFilter = new IntentFilter(NetworkChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                if (isNetworkAvailable) {
                    snackbar.dismiss();
                } else {
                    snackbar.show();
                    walkthroughLandingFragment.showProgressBar(false);
                }
            }
        }, networkIntentFilter);

        return receiver;
    }

    public static void unregisterNetworkListener(@NonNull Context context, NetworkChangeReceiver networkChangeReceiver) {
        try {
            context.unregisterReceiver(networkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

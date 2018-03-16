package com.plusmobileapps.safetyapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Robert Beerman on 2/24/2018.
 */

public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private Authenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call return the authenticator's
     * IBinder
     */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}

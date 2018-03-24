package com.plusmobileapps.safetyapp.data;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.PrefManager;

import java.util.concurrent.atomic.AtomicInteger;

public class ResponseUniqueIdFactory {

    public static int getId(){
        final PrefManager prefManager = new PrefManager(MyApplication.getAppContext());
        final int index = prefManager.getLastResponseUniqueId();
        AtomicInteger seq = new AtomicInteger(index);
        int lastId = seq.incrementAndGet();
        prefManager.setLastResponseUniqueId(lastId);
        return lastId;
    }

}

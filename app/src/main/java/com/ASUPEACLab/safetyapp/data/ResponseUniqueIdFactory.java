package com.ASUPEACLab.safetyapp.data;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.PrefManager;

import java.util.concurrent.atomic.AtomicInteger;

public class ResponseUniqueIdFactory {

    public static int getId() {
        final PrefManager prefManager = new PrefManager(MyApplication.getAppContext());
        final int index = prefManager.getLastResponseUniqueId();
        AtomicInteger seq = new AtomicInteger(index);
        int lastId = seq.incrementAndGet();
        prefManager.setLastResponseUniqueId(lastId);
        return lastId;
    }

}

package com.plusmobileapps.safetyapp.signup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.plusmobileapps.safetyapp.sync.DownloadCallback;

public class SchoolDownloadFragment extends Fragment {
    public static final String TAG = "SchoolDownloadFragment";

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    private DownloadCallback callback;
    private GetSchoolsTask getSchoolsTask;

    public static SchoolDownloadFragment getInstance(FragmentManager fragmentManager) {
        SchoolDownloadFragment schoolDownloadFragment = new SchoolDownloadFragment();
        Bundle args = new Bundle();
        schoolDownloadFragment.setArguments(args);
        fragmentManager.beginTransaction().add(schoolDownloadFragment, TAG).commit();
        return schoolDownloadFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        cancelGetSchools();
        super.onDestroy();
        callback = null;
    }

    public void getSchools() {
        cancelGetSchools();
        getSchoolsTask = new GetSchoolsTask(callback);
        getSchoolsTask.execute();
    }

    public void cancelGetSchools() {
        if(getSchoolsTask != null) {
            getSchoolsTask.cancel(true);
        }
    }

}

package com.plusmobileapps.safetyapp.data.singletons;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class Walkthroughs {
    private static final Walkthroughs ourInstance = new Walkthroughs();

    private AppDatabase db;
    private WalkthroughDao walkthroughDao;
    private List<Walkthrough> walkthroughList;
    public static Walkthroughs getInstance() {
        return ourInstance;
    }

    private Walkthroughs() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        walkthroughDao = db.walkthroughDao();
        populateWalkthroughs();
    }

    private void populateWalkthroughs() {
        walkthroughList = walkthroughDao.getAll();
    }

    //public methods
    public List<Walkthrough> getAllWalkthroughs() {
        return this.walkthroughList;
    }

    public Walkthrough getWalkthroughWithId(int walkthroughId) {
        for (int i = 0; i < walkthroughList.size(); i++) {
            Walkthrough tempWalkthrough = walkthroughList.get(i);
            if (tempWalkthrough.getWalkthroughId() == walkthroughId) {
                return tempWalkthrough;
            }
        }
        return null;
    }

    public void addWalkthrough(Walkthrough walkthrough) {
        walkthroughList.add(walkthrough);

        try {
            walkthroughDao.insert(walkthrough);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

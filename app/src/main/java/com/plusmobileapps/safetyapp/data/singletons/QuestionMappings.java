package com.plusmobileapps.safetyapp.data.singletons;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionMappingDao;
import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;

import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class QuestionMappings {
    private AppDatabase db;
    private QuestionMappingDao mappingDao;
    private List<QuestionMapping> mappingList;

    private static final QuestionMappings ourInstance = new QuestionMappings();

    public static QuestionMappings getInstance() {
        return ourInstance;
    }

    private QuestionMappings() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        mappingDao = db.questionMappingDao();
        populateMappings();
    }

    private void populateMappings() {
        mappingList = mappingDao.getAllMappings();
    }

    //public methods
    public List<QuestionMapping> getAllMappings() {
        return this.mappingList;
    }

    public QuestionMapping getMappingWithId(int mappingId) {
        for (int i = 0; i < mappingList.size(); i++) {
            QuestionMapping tempMapping = mappingList.get(i);
            if (tempMapping.getMappingId() == mappingId) {
                return tempMapping;
            }
        }
        return null;
    }

    public void addMapping(QuestionMapping mapping) {
        mappingList.add(mapping);

        try {
            mappingDao.insert(mapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.plusmobileapps.safetyapp.data.singletons;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.entity.User;

import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class Users {

    private AppDatabase db;
    private UserDao userDao;
    private User currentUser;
    private List<User> userList;

    private static final Users ourInstance = new Users();

    public static Users getInstance() {
        return ourInstance;
    }

    private Users() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        userDao = db.userDao();
        populateUsers();
    }

    private void populateUsers() {
        userList = userDao.getAllUsers();
    }

    //public methods
    public List<User> getAllUsers() {
        return userList;
    }

    public User getUserWithId(int userId) {
        for (int i = 0; i < userList.size(); i++) {
            User tempUser = userList.get(i);
            if (tempUser.getUserId() == userId) {
                return tempUser;
            }
        }
        return  null;
    }

    public void addUser(User user) {
        userList.add(user);

        try {
            userDao.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}

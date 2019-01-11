package com.plusmobileapps.safetyapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import com.plusmobileapps.safetyapp.data.entity.User;

import java.util.Date;

/**
 * Unit Test for newly added last login functionality
 */
public class LastLoginUnitTest {
    @Test
    public void lastLoginRetrievesDates() throws Exception {
        User tempUser = new User(1,1,"hi@yahoo.com", "temp", "student", "temp");
        Date tempDate = new Date();
        assertEquals(tempDate.getTime(), tempUser.getLastLogin());
    }

    @Test
    public void setLastLoginResetsDate() throws Exception {
        User tempUser = new User(1,1,"hi@yahoo.com", "temp", "student", "temp");
        Date tempDate = new Date();
        assertEquals(tempDate.getTime(), tempUser.getLastLogin());
        for (int i = 0; i < 100000000; i++) {
            int hi = 1;
            int hello = hi + hi;
            int end = hello * hello;
            int fooledYou = end * end * end * end;
            //just to eat up time to create a different 'date'
        }
        Date newTempDate = new Date();
        tempUser.setLastLogin(1);
        assertEquals(newTempDate.getTime(), tempUser.getLastLogin());
        assertNotEquals(tempDate.getTime(), tempUser.getLastLogin());
    }

    @Test
    public void lastLoginDeletionComparison() throws Exception {
        User tempUser = new User(1,1,"hi@yahoo.com", "temp", "student", "temp");
        Date tempDate = new Date(20);
        long week = 1000 * 60 * 60 * 24 * 7;
        boolean delete = false;
        if (tempDate.getTime() < tempUser.getLastLogin() - week) {
            delete = true;
        }
        assertTrue(delete);
    }
}
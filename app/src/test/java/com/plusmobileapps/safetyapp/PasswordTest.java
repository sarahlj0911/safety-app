package com.plusmobileapps.safetyapp;

import com.plusmobileapps.safetyapp.data.entity.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test for newly added password functionality
 */
public class PasswordTest {
    @Test
    public void passwordIsRetrievable() throws Exception {
        User tempUser = new User(1,1,"hi@yahoo.com", "temp", "student", "temp");
        assertEquals("temp", tempUser.getPassword());
    }

    @Test
    public void passwordCheckWorks() throws Exception {
        User tempUser = new User(1,1,"hi@yahoo.com", "temp", "student", "temp");
        assertFalse(tempUser.checkPassword("Temp"));
        assertTrue(tempUser.checkPassword("temp"));
    }
}
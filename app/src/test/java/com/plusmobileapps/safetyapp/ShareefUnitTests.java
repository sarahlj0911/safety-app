package com.plusmobileapps.safetyapp;

import com.plusmobileapps.safetyapp.signup.SignupActivity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ShareefUnitTests {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void schoolSpinner() {
       ArrayList<String> testy= new ArrayList<String>();
        new SignupActivity().populateSchoolSpinner(testy);
        assertEquals(testy,testy);
    }

}
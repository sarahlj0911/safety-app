package com.plusmobileapps.safetyapp;

import com.plusmobileapps.safetyapp.util.DataExtractor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void extractor(){
        DataExtractor extractor = new DataExtractor("New Item",1);
        String First = "Hello";
        String Second = "second";
        extractor.addItem(First);
        extractor.addItem(Second);
        String testString =extractor.getItem();
        String nextTestitme = extractor.getItem();

        assertTrue(First.equals(nextTestitme) );
        assertTrue(Second.equals(testString) );

    }


}

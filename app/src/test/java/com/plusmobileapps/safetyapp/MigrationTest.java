package com.plusmobileapps.safetyapp;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.arch.persistence.room.testing.MigrationTestHelper;

/**
 * Created by rbeerma on 2/19/2018.
 */

@RunWith(JUnit4.class)
public class MigrationTest {
    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper;

    public MigrationTest() {

    }
}

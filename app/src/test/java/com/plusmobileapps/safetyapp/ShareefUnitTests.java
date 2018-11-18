package com.plusmobileapps.safetyapp;
import com.plusmobileapps.safetyapp.data.entity.Admin;
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
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void schoolSpinner() throws Exception {
       ArrayList<String> testy= new ArrayList<String>();
        new SignupActivity().populateSchoolSpinner(testy);
        assertEquals(testy,testy);
    }
    @Test
    public void adminTest() throws Exception {
        Admin c = new Admin(1,22,"s","asas","ad");
        c.setEmailAddress("bobby@yahoo.com");
        c.setRemoteId(3);
        c.setUserName("bobby");
        c.setSchoolId(1111);
        c.setUserId(22);
        c.setRole("admin");
        //asserts
        assertEquals(3,c.getRemoteId());
        assertEquals("bobby@yahoo.com",c.getEmailAddress());
        assertEquals("bobby",c.getUserName());
        assertEquals(1111,c.getSchoolId());
        assertEquals(22,c.getUserId());
        assertEquals("admin",c.getRole());
    }
}
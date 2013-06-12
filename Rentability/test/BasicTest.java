import models.*;

import org.junit.*;

import controllers.Application;

import java.util.*;
import play.test.*;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }
    
    @Test
    public void CreateUserTest(){
    	User user = new User("Marcel", "Altendeitering", "m.altendeitering@tum.de", "012345678", "Massi1000", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5");
    	User retrievedUser = User.findById(user.id);
    	assertEquals("Marcel", retrievedUser.first_name);
    	assertEquals("m.altendeitering@tum.de", retrievedUser.email);
    	assertEquals("5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", retrievedUser.passwordHash);
    }
    
    @Test
    public void CheckHashFunction(){
    	String correctHashOf12345 = "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5";
    	String actualHash = Application.getHash("12345", "SHA-256");
    	assertEquals(correctHashOf12345, actualHash);
    }

}

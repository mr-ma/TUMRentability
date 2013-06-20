import java.util.*;

import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
    @Test
    public void testRegisterPage(){
    	Response r = GET("/register");
    	assertIsOk(r);
    	assertContentType("text/html", r);
    	assertCharset(play.Play.defaultWebEncoding, r);
    }
    
    @Test
    public void testAboutUsPage(){
    	Response r = GET("/application/aboutUs");
    	assertIsOk(r);
    	assertContentType("text/html", r);
    	assertCharset(play.Play.defaultWebEncoding, r);
    }
    
    @Test
    public void testPrivacyPage(){
    	Response r = GET("/application/privacyPolicy");
    	assertIsOk(r);
    	assertContentType("text/html", r);
    	assertCharset(play.Play.defaultWebEncoding, r);
    }
    
    @Test
    public void testCreateOfferPage(){
    	Response r = GET("/offers/createOffer");
    	//Since not logged in, we're expecting a redirection (Code 302)
    	assertStatus(302, r);
    }
    
    @Test
    public void testLogin(){
    	Map<String, String> loginUserParams = new HashMap<String, String>();
    	loginUserParams.put("username", "Demo");
    	loginUserParams.put("password", "demo");
    	Response r = POST("/secure/login", loginUserParams);
    	assertIsOk(r);
    	assertContentType("text/html", r);
    	assertCharset(play.Play.defaultWebEncoding, r);
    }
    
}
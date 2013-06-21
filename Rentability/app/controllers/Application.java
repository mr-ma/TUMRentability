package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.cache.*;
import tools.Mailing;

import java.util.*;
import java.security.*;

import org.apache.commons.mail.*;
import org.apache.commons.mail.Email;

import models.*;


public class Application extends Controller {
	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) 
        {
            User user = User.find("byEmail", Security.connected()).first();
         // I'm passing nickname change if you need something else
            if(user!= null)
            { renderArgs.put("user", user);
            }
        }
        else 
    	{renderArgs.put("user", null);
    	}
    }
	

	//Rendering the index page
    public static void index() {
    	
    	List<Offer> offers = Inventory.getAllOffers();
    	
    	render(offers);
    }
    
    
    
   public static void offerDetails(Long id){
    	Offer offer = Offer.findById(id);
    	List<Request> requests = Offer.getAllRequests(offer);
    	String insurance = "No";
    	if (offer.insurance){
    		insurance = "Yes";
    	}
    	String reservedDatesEmpty = "";
    	if (requests.isEmpty()){
    		reservedDatesEmpty = "Currently there are no other requests for this offer.";
    	}
    	render(offer,requests,insurance,reservedDatesEmpty);
    }
    
    
    
    
    //Rendering the registration page (and generating a unique ID for the captcha image)
    public static void register() {
    	String randomID = Codec.UUID();
        render(randomID);
    }
    
    //Rendering the Privacy Policy page
    public static void privacyPolicy()
    {
    	render();
    }
    
    //Rendering the About Us page
    public static void aboutUs()
    {
    	render();
    }
    
    //Registration of a new User
    public static void saveUser(@Valid User user, String verifyPassword, @IsTrue String policyAgreement, @Required String code, String randomID) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        //Email must end with the string "tum.de"
        validation.match(user.email, ".+tum.de").message("Sorry, only TUM Mail Addresses are valid!");
        //Comparing the entered validation code with the one from cache using the given randomID
        validation.equals(code, Cache.get(randomID)).message("Invalid Code, please type again!");
        if(validation.hasErrors()) {
            render("@register", user, verifyPassword, randomID);
        }
        user.create();
        session.put("user", user.email);
        if(Security.authenticate(user.email, user.password)){
        	flash.success("Registration success! Please login and start renting Sports equipment right away!");  
        }
        
        user.confirmationCode = Codec.UUID();
        Mailing.sendConfirmationMail(user);
        
        index();
        
    }
    
    //Generation of captcha images using the play libraries
    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        //#000000 (ie black) represents the color of the captcha text
        String code = captcha.getText("#000000");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }
    
    public static void confirm(String code)
    {
    	System.out.println(code);
    }
    
    //Generating a hash value using a given method for some data
    public static String getHash(String data, String hashMethod)
    {
    	MessageDigest md;
		try {
			//Setting the Hashfunction to SHA-256 (MD5 and others are available as well)
			md = MessageDigest.getInstance("SHA-256");
			
			//Setting the data --> can be done more than once
			md.update(data.getBytes());
			
			//generating the Hash
			byte[] byteData = md.digest();
			
			//generating a hex representation
			StringBuffer sb = new StringBuffer();
			
	        for (int i = 0; i < byteData.length; i++) {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        return sb.toString();
			
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
    }

}

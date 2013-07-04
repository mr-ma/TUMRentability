package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import models.Category;
import models.Offer;
import models.Request;
import models.User;
import play.cache.Cache;
import play.data.validation.IsTrue;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.i18n.Lang;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;
import play.test.Fixtures;
import tools.Mailing;


public class Application extends Controller {
	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) 
        {
            User user = User.find("byEmail", Security.connected()).first();
         // I'm passing nickname change if you need something else
            if(user!= null)
            { 
            	renderArgs.put("user", user);
            	
            	///Find unseen requests and put count of it to the response
            	List<Request> userrequests=  Request.find("offer.article.owner.id", user.id).fetch();
            	int unseenRequests=0;
            	for(int i=0;i<userrequests.size();i++){
            		if(!userrequests.get(i).seen) unseenRequests++;
            	}
            	renderArgs.put("unseenRequests", unseenRequests);
            }
        }
        else 
    	{renderArgs.put("user", null);
    	}
    }
	
	@Before
	static void addDefaults() {
	    renderArgs.put("mainCates", Inventory.getAllMainCategories());
	}
	
//	@Before
//	static void setLanguage() {
//		Lang.change("de");
//		
//	}
	

	//Rendering the index page
    public static void index() {
     	List<Offer> newestOffers = Offer.find("select o from Offer o where o.state != '-1' order by publicationTime desc").fetch(5);
    	List<Offer> mostPopularOffers = Offer.find("select o from Offer o where o.state != '-1' order by countReviews desc").fetch(5);
    	List<Offer> offers = Inventory.getAllOffers();
    	render(offers,newestOffers,mostPopularOffers);
    }
    
    public static void setLanguageEN(){
    	Lang.change("en");
    	index();
    }
    public static void setLanguageDE(){
    	Lang.change("de");
    	index();
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
    
    /**
     * Rendering the registration page (and generating a unique ID for the captcha image)*/
    public static void register() {
    	String randomID = Codec.UUID();
        render(randomID);
    }
    
    /**
     * Rendering the Pending page*/
    public static void pending() {
    	render();
    }
    
    /**
     * Rendering the Privacy Policy page*/
    public static void privacyPolicy() {
    	render();
    }
    
    /**
     * Rendering the About Us page*/
    public static void aboutUs() {
    	render();
    }
    
    /**
     * Registration of a new User
     * @param user the User including all inserted values
     * @param verifyPassword The verification password
     * @param policyAgreement true if the checkbox has been clicked
     * @param code The inserted registration code
     * @param randomID The random id used to generate the captcha*/
    public static void saveUser(@Valid User user, String verifyPassword, @IsTrue String policyAgreement, @Required String code, String randomID) {
    	validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        //Email must end with the string "tum.de"
        validation.match(user.email, ".+tum.de").message("Sorry, only TUM Mail Addresses are valid!");
        //Comparing the entered validation code with the one from cache using the given randomID
        validation.equals(code, Cache.get(randomID)).message("Invalid Code, please type again!");
        
        //Check if all required fields are correct
        if(validation.hasErrors()) {
            render("@register", user, verifyPassword, randomID);
        }
        
//        //Check if User already exists
//        if(User.getUserByEmail(user.email) != null) {
//        	flash.error("You have already registered, please log in!");
//        	index();
//        }
        
        //Hash the password
        user.password = getHash(user.password, "SHA-256");
        user.confirmationCode = Codec.UUID();
        user.activated = false;
        user.create();
//        session.put("user", user.email);
//        if(Security.authenticate(user.email, user.password)){
//        	flash.success("Registration success! Please login and start renting Sports equipment right away!");  
//        }
        
        Mailing.sendConfirmationMail(user);
        
        pending();
        
    }
    
    /**
     * Generates a captcha image for the registration process
     * 
     * @param id A unique id needed to generate the captcha
     * */
    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        //#000000 (ie black) represents the color of the captcha text
        String code = captcha.getText("#000000");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }
    
    /**
     * Confirmation of a user who has just registered.
     * Is invoked whenever a user clicks the confirm link in the confirmation email.
     * 
     * @param code Confirmation code for the user
     * */
    public static void confirm(String code)
    {
    	User user = User.getUserByConfirmCode(code);
    	if(user != null) {
    		flash.success("Your Registration has been confirmed! Please login and start renting Sports equipment right away!");
    		user.confirmationCode = "";
    		user.activated = true;
    		user.save();
    	}
    	else
    		flash.error("Sorry, no account has been found!");
    	
    	index();
    }
    
    /**
     * Creates a hash representation of a given input string
     * 
     * @param data The input string that should be hashed
     * @param hashMethod The way in which the string should be hashed (e.g. SHA-256, MD5, etc.)
     * @return A hash representation of the "data" String
     * */
    public static String getHash(String data, String hashMethod)
    {
    	MessageDigest md;
		try {
			//Setting the Hashfunction to SHA-256 (MD5 and others are available as well)
			md = MessageDigest.getInstance(hashMethod);
			
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
			e.getMessage();
			return "";
		}
    }

}

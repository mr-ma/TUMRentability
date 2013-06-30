package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import play.db.jpa.Blob;
import play.libs.Mail;
import play.mvc.*;
import tools.Mailing;

import models.*;
import play.data.validation.*;
@With(Secure.class)
public class Rentability extends Controller {

	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();

//            if(user!= null)
                renderArgs.put("user", user);
//            else 
//            	renderArgs.put("user", "guest");
        }
    }
	
	@Before
	static void addDefaults() {
	    renderArgs.put("mainCates", Inventory.getAllMainCategories());
	}

	
	//Rendering the (personalized) index page
	public static void index() {
		
		/*Personalized content to be implemented!*/
		
        render();
    }
    
	//Rendering the create offer page using all existing categories
    public static void createOffer() {
    	List<Category> categories = Inventory.getAllMainCategories();
    	render(categories);
    }
    
    public static void reloadSubCate(String name) {
    	long cateID = Long.parseLong(name);
    	List<Category> subCates = Inventory.getSubCategories(cateID);

        render("@selectSubCate", subCates);
    }
    
    //Creating a new Offer
    public static void saveOffer(Blob image, String articleName, String description, String name, 
    		String pickUpAddress, String startTime, String endTime, String price, String insurance) {    	
    	
    	validation.required(articleName);
    	validation.required(description);
    	validation.required(pickUpAddress);
    	validation.required(startTime);
    	//Make sure the date is entered in this format DD.MM.YYYY
    	validation.match(startTime, "\\d{2}\\.\\d{2}\\.\\d{4}").message("Please indicate the Date in the given format!");
    	validation.required(endTime);
    	validation.match(endTime, "\\d{2}\\.\\d{2}\\.\\d{4}").message("Please indicate the Date in the given format!");
    	validation.required(price);
    	//Ensures that the price field is followed by 2 digits after the point
    	validation.match(price, "\\d+\\.\\d{2}").message("Please indicate the Price in the given format!");
    	
    	if(validation.hasErrors())
    	{
    		System.out.println(image.getFile().getAbsolutePath());
    		List<Category> categories = Category.findAll();
    		render("@createOffer", articleName, description, categories, pickUpAddress,
    				startTime, endTime, price, insurance);
    	}
    	else
    	{
    		boolean insuranceRequired;
    		
    		List<Category> categories = Category.findAll();
        	Category c = categories.get(Integer.valueOf(name) - 1);
        	
        	//Retrieving the logged in user
        	User u = new User("","","","","","");
        	//User u = (User)renderArgs.get("user");
        	
        	//null value to be implemented - represents the user (ie owner)
        	Article a = new Article(articleName, description, u, c, image);
        	
        	//Conversion of String Values to Dates, Boolean, etc.
        	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			try {
				Date start = sdf.parse(startTime);
				Date end = sdf.parse(endTime);
				
				if(insurance == null || !insurance.equals("true"))
					insuranceRequired = false;
				else
					insuranceRequired = true;
				
				double doublePrice = Double.parseDouble(price);
				
				//null value to be implemented - represents the description
	        	new Offer(pickUpAddress, insuranceRequired, 0, doublePrice, null, start, end, a);
	        	
	        	flash.success("Your offer has successfully been created!");
	        	Application.index();
	        	
			} catch (Exception ex) {
				flash.error("Sorry an error occured, please try again!");
			}
    	}
    }
    
    // Creating a new request:
    public static void saveRequest(double adjustedPrice, String startTime, String endTime, Long offerID) {    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
	        Offer offer = Offer.findById(offerID);
	        boolean duplicate = false;
	        Request duplicatedRequest = null;
			Date start = sdf.parse(startTime);
			Date end = sdf.parse(endTime);
			for (Request oldRequest : Offer.getAllRequests(offer)){
				if (oldRequest.startTime.equals(start)){
					duplicate = true;
					duplicatedRequest = oldRequest;
				}
			}
			User requestingUser;
	        if(renderArgs.get("user") != null) {
	        	requestingUser = renderArgs.get("user", User.class);
	        }
	        else {
	        	String username = session.get("user");
	            if(username != null) {
	            	requestingUser = User.find("byUsername", username).first();
	            } 
	            else requestingUser = null;
	        }
	        Request requested;
	        short state = 1;
			if (!duplicate) {requested = new Request(state,adjustedPrice,start,end,offer,requestingUser);}
			else {
				requested = duplicatedRequest;
			}
			
			//Sending the newRequest Email
			Mailing.newRequest(requestingUser, requested);
			
			showRequest(requested);

			
		} catch (Exception ex) {
			flash.error("Sorry an error occured, please try again!");
		}
    }
    
    public static void showRequest(Request requested){
    	render(requested);
    }
    
  //go to user Management page
    public static void userManagement()
    {
    	render();
    }
    //go to user Management page
    public static void userManagement2(long id)
    {
    	User user = User.find("byId", id).first();
    	renderArgs.put("nick_name",user.nick_name );
    	renderArgs.put("first_name", user.first_name);
    	renderArgs.put("last_name", user.last_name);
    	renderArgs.put("phone", user.phone);
    	render("@userManagement");
    }
    public static void updateUser(@Required String nick_name, @Required String first_name, @Required String last_name,String phone)
    {
    	
    	validation.required(nick_name).message("Field is required");
    	validation.required(first_name);
    	validation.required(last_name);
    	   if(validation.hasErrors()){
        
//        	render("@userManagement",nick_name,first_name,last_name,phone);
        	render("@userProfile",nick_name,first_name,last_name,phone);
    	   }
            else
            {
            User user = User.find("byEmail", Security.connected()).first();     
            user.nick_name = nick_name;
            user.first_name = first_name;
            user.last_name= last_name;
            user.phone= phone;
            user.save();
            flash.success("Successfully updated profile");
//            userManagement2(user.id);
            userProfile2(user.id);
            }
           
        }
  //go to user Management page
    public static void userProfile()
    {
    	render();
    }
    //go to user Management page
    public static void userProfile2(long id)
    {
    	User user = User.find("byId", id).first();
    	renderArgs.put("nick_name",user.nick_name );
    	renderArgs.put("first_name", user.first_name);
    	renderArgs.put("last_name", user.last_name);
    	renderArgs.put("phone", user.phone);
    	render("@userProfile");
    }

    public static void userArticles()
    {
    	if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            List<Article> userArticles=  Article.find("owner.id", user.id).fetch();
            renderArgs.put("articles", userArticles);
            render();
    	}
    }
    
    public static void articleOffers(long articleId)
    {
    	Article article= Article.findById(articleId);
    	List<Offer> offers= Article.getAllOffers(article);
    	renderArgs.put("article", article);
    	renderArgs.put("offers", offers);
    	render();
    }
    
  public static void offerRequests(long offerId)
  {
	Offer offer= Offer.findById(offerId);
  	List<Request> requests= Offer.getAllRequests(offer);
  	boolean alreadyApproved=false;
  	for(int i=0;i<requests.size();i++)
  	{
  	if(requests.get(i).state ==5) alreadyApproved=true; 
  	}
  	renderArgs.put("alreadyApproved",alreadyApproved);
  	renderArgs.put("offer", offer);
  	renderArgs.put("requests", requests);
  	render();
  }
  
  public static void approveRequest(long requestId,long offerId)
  {
	  Request request= Request.findById(requestId);
	  request.state=5;
	  request.save();
	  Offer offer= Offer.findById(offerId);
	  offer.state= -1;
	  offerRequests(offerId);
	  
  }
  
//Rendering the create offer page using all existing categories
  public static void createOfferWithArticle(long articleId) {
  	List<Category> categories = Category.findAll();
  	Article article= Article.findById(articleId);
  	session.put("article", articleId);
  	renderArgs.put("article", article);
  	render(categories);
  }
  //Creating a new Offer
  public static void saveOfferOfArticle(String description, String name, 
  		String pickUpAddress, String startTime, String endTime, String price, String insurance) {    	
  	
	  
	Article article =Article.findById(Long.parseLong(session.get("article"))); 
  	validation.required(description);
  	validation.required(pickUpAddress);
  	validation.required(startTime);
  	//Make sure the date is entered in this format DD.MM.YYYY
  	validation.match(startTime, "\\d{2}\\.\\d{2}\\.\\d{4}").message("Please indicate the Date in the given format!");
  	validation.required(endTime);
  	validation.match(endTime, "\\d{2}\\.\\d{2}\\.\\d{4}").message("Please indicate the Date in the given format!");
  	validation.required(price);
  	//Ensures that the price field is followed by 2 digits after the point
  	validation.match(price, "\\d+\\.\\d{2}").message("Please indicate the Price in the given format!");
  	
  	if(validation.hasErrors())
  	{
  		List<Category> categories = Category.findAll();
  		render("@createOfferWithArticle" ,article, description, categories, pickUpAddress,
  				startTime, endTime, price, insurance);
  	}
  	else
  	{
  		boolean insuranceRequired;
      	//Conversion of String Values to Dates, Boolean, etc.
      	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			try {
				Date start = sdf.parse(startTime);
				Date end = sdf.parse(endTime);
				
				if(insurance == null || !insurance.equals("true"))
					insuranceRequired = false;
				else
					insuranceRequired = true;
				
				double doublePrice = Double.parseDouble(price);
				
				//null value to be implemented - represents the description
	        	new Offer(pickUpAddress, insuranceRequired, 0, doublePrice, description, start, end, article);
	        	
	        	flash.success("Your offer has successfully been created!");
	        	Application.index();
	        	
			} catch (Exception ex) {
				flash.error("Sorry an error occured, please try again!");
			}
  	}
  }

  public static void removeOffer(long offerId,long articleId)
  {
	  //if offer has requests can not be deleted
		Offer offer= Offer.findById(offerId);
		int requestssize=offer.getAllRequests(offer).size();
		if(requestssize>0)
		{
			flash.error("This offer has "+requestssize +" rent requests(s)! Can not be removed" );
		}
		else
		{
		///remove offer
			offer.delete();
		}
			
		articleOffers(articleId); 
  }
  public static void removeArticle(long articleId)
  {
	  //if article has offer can not be deleted
		Article article= Article.findById(articleId);
		int offerssize=Article.getAllOffers(article).size();
		if(offerssize>0)
		{
			flash.error("This article has "+offerssize +" offer(s)! First remove offers in order to be able remove the article" );
		}
		else
		{
		///remove article
			article.delete();
		}
		userArticles();
  }
  
  
  ///Provide requests of current user and their state approved or pending
  public static void userRequests()
  {
	  if(Security.isConnected()) {
          User user = User.find("byEmail", Security.connected()).first();
          List<Request> userRequest=  Request.find("user.id", user.id).fetch();
          renderArgs.put("requests", userRequest);
          render();
  	}
  }
  
  ///Provide requests which have been sent to current user equipments from other users
  public static void userReceivedRequests()
  {
	  if(Security.isConnected()) {
          User user = User.find("byEmail", Security.connected()).first();
          List<Request> userRequest=  Request.find("offer.article.owner.id", user.id).fetch();
       
          ///iterate to change seen to true so that, notification in main page will be removed 
          for(int i=0;i<userRequest.size();i++)
         {
        	 if(!userRequest.get(i).seen) 
        		 {
        		 	userRequest.get(i).seen=true;
        		 	userRequest.get(i).save();
        		 }
         }
          renderArgs.put("requests", userRequest);
          render();
  	}
  }
  public static void changePassword()
  {
	  
	  render();
  }
  
  public static void updatePassword(@Required String currentpassword,@Required String newpassword,@Required String verifypassword)
  {
		validation.required(currentpassword).message("Field is required");
    	validation.required(newpassword).message("Field is required");
    	validation.required(verifypassword).message("Field is required");
    	 validation.equals(verifypassword, newpassword).message("Your password doesn't match");
    	   if(validation.hasErrors()){
        	render("@changePassword",currentpassword);
    	   }
            else
            {
            	User user = User.find("byEmail", Security.connected()).first();     
            	if( user.password.equals(Application.getHash(currentpassword, "SHA-256"))) {
            		user.password= Application.getHash(newpassword,"SHA-256");
            		user.save();
            		flash.success("Successfully updated your password");
            		changePassword();
            	}
            	else
            	{
            		flash.error("Entered password is not correct ");
            		render("@changePassword",currentpassword);
            	}
            }
  }
}

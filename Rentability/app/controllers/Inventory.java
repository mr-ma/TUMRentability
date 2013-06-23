package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Article;
import models.Category;
import models.Offer;
import models.Request;
import models.User;
import play.mvc.*;

public class Inventory extends Controller {

    public static void index() {
        render();
    }
    /**
     * Get the image of an article in the db
     * 
     * @param id the id of the article
     */
    public static void articleImage(long id) { 
 	   //long id = 1;
 	   final Article article = Article.findById(id); 
 	   response.setContentTypeIfNotSet(article.image.type());
 	   java.io.InputStream binaryData = article.image.get();
 	   renderBinary(binaryData);
 	}
    
    /**
     * Get all articles that belong to a User
     * 
     * @param id The id of the owner of the articles.
     * @return A list containing all the articles of the User
     */
    public static List<Article> getArticlesByOwner(long id){
    	
    	List<Article> articles =  Article.find("byOwner", User.findById(id)).fetch();
    	
    	return articles;
    	
    	
    }
    
    /**
     * Get All the offers that are currently in the system.
     * 
     * @return A list of all the offers in the system.
     */
    public static List<Offer> getAllOffers(){
    	
    	return Offer.findAll();
    	
    }
    
    /**
     * Get All the requests currently in the system.
     * 
     * 
     * @return A list of all the requests that are in the system.
     */
    public static List<Request> getAllRequests(){
    	
    	return Request.findAll();
    	
    }
    
    /**
     * Get all the main categories that are in the system.
     * 
     * @return A list of all the main categories in the system.
     */
    public static List<Category> getAllMainCategories(){
    	
    	return  Category.find("parent_category is null").fetch();
    	
    }
    
    /**
     * Get all the sub categories that are in the system.
     * 
     * @return A list of all the sub categories in the system.
     */
    public static List<Category> getAllSubCategories(){
    	
    	return Category.find("parent_category is not null").fetch();
    }
    /**
     * Get all the sub categories that belong to a main category.
     * 
     * @param id_main_category The id of the main category.
     * @return A list of all the sub categories that belong to the specified main category.
     */
    public static List<Category> getSubCategories(long id_main_category){
    	
    	return Category.find("select c from Category c where c.parent_category.id = " + id_main_category).fetch();
    	
    }
    /**
     * Get all the offers that belong to a User
     * 
     * @param id_owner The id of the owner of the offer.
     * @return A list of all the offers that belong to the specified user.
     */
    public static List<Offer> getOffersByOwner(long id_owner){
    	
    	return Offer.find("select o from Offer o where o.article.owner.id = " + id_owner).fetch();
    	
    	
    }
    
    /**
     * Get all the offers that are part of a category (either main or sub)
     * 
     * @param id_category the id of the category
     * @return A list of all the offers that are part of the specified category.
     */
    public static List<Offer> getOffersByCategory(long id_category){
    	
    	Category cat = Category.findById(id_category);
    	
    	//is it a sub category?
    	if(cat.parent_category != null){
    		
    		return Offer.find("select o from Offer o where o.article.category.id = " +id_category).fetch();
    		
    	//its a main category
    		
    	}else{
    		
    		List<Category> sub = getSubCategories(id_category);
    		
    		List<Offer> offers = new ArrayList<Offer>();
    		
    		for(Category c : sub){
    			
    			List<Offer> temp = Offer.find("select o from Offer o where o.article.category.id = "+ c.id).fetch();
    			offers.addAll(temp);
    		}
    		
    		return offers;
    		
    		
    	}
    }
    /**
     * Get a list of all the requests made by a user.
     * 
     * @param id_user The id of the user that made a request.
     * @return A list of all the requests belonging to a user.
     */
    public static List<Request> getRequestsByUser(long id_user){
    	
    	
    	return Request.find("select r from Request where r.user = "+id_user).fetch();
    	
    }
    
    /**
     * Get all the requests that have been made for an offer.
     * 
     * @param id_offer the id of the offer for which to get the requests.
     * @return A list of all the requests made for the specified offer.
     */
    public static List<Request> getRequestbyOffer(long id_offer){
    	
    	return Request.find("select r from Request where r.offer.id = "+ id_offer).fetch();
    	
    }
    

}

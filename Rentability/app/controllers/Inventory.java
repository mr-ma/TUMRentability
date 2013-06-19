package controllers;

import java.util.List;

import models.Article;
import models.Offer;
import models.User;
import play.mvc.*;

public class Inventory extends Controller {

    public static void index() {
        render();
    }
    
    public static void articleImage(long id) { 
 	   //long id = 1;
 	   final Article article = Article.findById(id); 
 	   response.setContentTypeIfNotSet(article.image.type());
 	   java.io.InputStream binaryData = article.image.get();
 	   renderBinary(binaryData);
 	}
    
    
    public static List<Article> getArticlesByOwner(long id){
    	
    	List<Article> articles =  Article.find("byOwner", User.findById(id)).fetch();
    	
    	return articles;
    	
    	
    }
    
    
    public static List<Offer> getAllOffers(){
    	
    	return Offer.findAll();
    	
    }
    
    //Working on it, requires a proper query
    //public static List<Offer> getOffersByOwner(long id){}
    //public static List<Offer> getOffersByCategory(long id){}
    

}

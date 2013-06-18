package controllers;

import java.util.*;

import javax.validation.*;

import play.mvc.*;

import models.*;

public class Rentability extends Application {

//  @Before
//  static void checkUser() {
//      if(connected() == null) {
//          flash.error("Please log in first");
//          Application.index();
//      }
//  }
	
	
	//Rendering the (personalized) index page
	public static void index() {
		
		/*Personalized content to be implemented!*/
		
        render();
    }
    
	//Rendering the create offer page using all existing categories
    public static void createOffer() {
    	List<Category> categories = Category.findAll();
    	render(categories);
    }
    
    //Creating a new Offer
    public static void saveOffer(@Valid Article article, @Valid Category cat, @Valid Offer offer) {    	
    	if(validation.hasErrors())
    	{
    		render("@createOffer", article, cat, offer);
    	}
    	else
    	{
    		article.create();
    		offer.create();
    	}
    }
}

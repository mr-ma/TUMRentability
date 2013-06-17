package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {

	public String first_name;
	
	public String last_name;
	
	public String email;
	
	public String phone;
	
	public String nick_name;

	public String passwordHash; //SHA-256 Hash
	
	public User(String first_name, String last_name, String email, String phone, String nick_name, String passwordHash) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.nick_name = nick_name;
		this.email = email;
		this.phone = phone;
		this.passwordHash = passwordHash;
		create();
	}

	public static List<Article> getAllArticles (User user){
		return Article.find("byOwner",user).fetch();
	}

	public static List<Offer> getAllOffers (User user){
		List<Offer> offers = new ArrayList<Offer>();
		for (Article article : User.getAllArticles(user)){
    		offers.addAll(Article.getAllOffers(article));
    	}
    	return offers;
	}

	public static List<Request> getAllOfferRequests (User user){
		List<Request> requests = new ArrayList<Request>();
		for (Offer offer : User.getAllOffers(user)){
    		requests.addAll(Offer.getAllRequests(offer));
    	}
    	return requests;
	}

	public static List<Request> getAllRequests (User user){
		return Request.find("byUser",user).fetch();
	}
	
	
	
	
    
}

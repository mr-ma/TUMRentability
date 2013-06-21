package models;

import play.*;
import play.data.validation.Email;
import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {

	@Required
	@MaxSize(50)
	@MinSize(2)
	public String first_name;
	
	@Required
	@MaxSize(50)
	@MinSize(2)
	public String last_name;
	
	@Required
	@MaxSize(100)
	@MinSize(4)
	@Email
	public String email;
	
	@MaxSize(15)
	@MinSize(4)
	public String phone;
	
	@Required
	@MaxSize(30)
	@MinSize(4)
	public String nick_name;
	
	@Required
	@MaxSize(30)
	@MinSize(8)
	public String password; //SHA-256 Hash
	
	public String confirmationCode;
	
	public User(String first_name, String last_name, String email, String phone, String nick_name, String passwordHash) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.nick_name = nick_name;
		this.email = email;
		this.phone = phone;
		this.password = passwordHash;
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

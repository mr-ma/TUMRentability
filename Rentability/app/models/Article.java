package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Article extends Model {
    
	public String name;
	
	public String description;
	
	public Blob image;
	
	@ManyToOne(optional = false)
	public User owner;
	
	@ManyToOne(optional = false)
	public Category category;

	public Article(String name, String description, User owner, Category category, Blob image) {
		super();
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.category = category;
		this.image = image;
		create();
	}
	
	public static List<Offer> getAllOffers(Article article){
		return Offer.find("byArticle",article).fetch();
	}
	
	public static List<Request> getAllRequests(Article article){
		List<Request> requests = new ArrayList<Request>();
    	for (Offer offer : Article.getAllOffers(article)){
    		requests.addAll(Offer.getAllRequests(offer));
    	}
    	return requests;
	}
}

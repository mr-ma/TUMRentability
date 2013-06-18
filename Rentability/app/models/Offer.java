package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Offer extends Model {

	@Required
	public String pick_up_address;
	
	public boolean insurance;
	
	public int state;
	
	@Required
	@Column(precision=6, scale=2)
	public double price;
	
	public String description;
	
	@Required
	public Date startTime;
	
	@Required
	public Date endTime;
	
	@ManyToOne(optional = false)
	public Article article;
	
	/* 
	@OneToMany (mappedBy = "offer")
	public Set<Request> requests;
	*/
	
	public Offer(String pick_up_address, boolean insurance, int state, double price, String description, Date startTime, Date endTime, Article article) {
		super();
		
		this.pick_up_address = pick_up_address;		
		this.insurance = insurance;		
		this.state = state;		
		this.price = price;		
		this.description = description;		
		this.startTime = startTime;		
		this.endTime = endTime;		
		this.article = article;	
		
		create();
	}
	
	public static List<Request> getAllRequests(Offer offer){
		return Request.find("byOffer",offer).fetch();
	}
}

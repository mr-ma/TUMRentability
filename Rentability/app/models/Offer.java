package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Offer extends Model {

	public String pick_up_address;
	public boolean insurance;
	public short state;
	public double price;
	public String description;
	public Date startTime;
	public Date endTime;
	
	@ManyToOne(optional = false)
	public Article article;
	
	@OneToMany
	public Set<Request> requests;

	
	public Offer(String pick_up_address, boolean insurance, short state, double price, String description, Date startTime, Date endTime, Article article) {
		super();
		
		this.pick_up_address = pick_up_address;		
		this.insurance = insurance;		
		this.state = state;		
		this.price = price;		
		this.description = description;		
		this.startTime = startTime;		
		this.endTime = endTime;		
		this.article = article;	
		this.requests = null;
		
		create();
	}
	
	
	
	
	
	
	
    
}

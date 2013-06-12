package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;


@Entity
public class Request extends Model {

	public short state;
	public double adjustedPrice;
	public Date startTime;
	public Date endTime;
	
	// Need to discuss this tomorrow
	@ManyToOne(optional = false)
	public Offer offer;
	
	@ManyToOne(optional = false)
	public User user;
		
	public Request(short state, double adjustedPrice, Date startTime, Date endTime, Offer offer, User user) {
		super();
		
		this.state = state;		
		this.adjustedPrice = adjustedPrice;		
		this.startTime = startTime;		
		this.endTime = endTime;		
		this.offer = offer;	
		this.user = user;
		
		create();
	}
	
	
	
	
	
	
	
    
}

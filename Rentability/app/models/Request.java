package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;


@Entity
public class Request extends Model {

	
	@Required
	public short state;
	
	@Required
	@Column(precision=6, scale=2)
	public double adjustedPrice;
	
	@Required
	@Temporal(TemporalType.DATE) 
	public Date startTime;

	@Required
	@Temporal(TemporalType.DATE) 
	public Date endTime;
	
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

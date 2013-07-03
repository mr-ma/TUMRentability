package models;

import play.*;

import play.data.validation.*;
import play.db.jpa.*;
import play.modules.elasticsearch.annotations.ElasticSearchEmbedded;
import play.modules.elasticsearch.annotations.ElasticSearchIgnore;
import play.modules.elasticsearch.annotations.ElasticSearchable;

import org.elasticsearch.*;

import javax.persistence.*;

import java.util.*;

@Entity
@ElasticSearchable
public class Offer extends Model {

	@ElasticSearchIgnore
	@Required
	public String pick_up_address;
	
	@ElasticSearchIgnore
	@Required
	public boolean insurance;
	
	@ElasticSearchIgnore
	@Required
	public int state;
	
	@ElasticSearchIgnore
	@Required
	public int countReviews;
	
	@ElasticSearchIgnore
	@Required
	@Column(precision=6, scale=2)
	public double price;
	
	@Required
	public String description;
	
	@ElasticSearchIgnore
	@Required
	@Temporal(TemporalType.DATE) 
	public Date startTime;
	
	@ElasticSearchIgnore
	@Required
	@Temporal(TemporalType.DATE) 
	public Date endTime;
	
	@ElasticSearchIgnore
	@Required
	@Temporal(TemporalType.DATE) 
	public Date publicationTime;
	
	@ElasticSearchEmbedded(fields={"name", "description"})
	@ManyToOne(optional = false)
	public Article article;

	
	public Offer(String pick_up_address, boolean insurance, int state, double price, String description, Date startTime, Date endTime, Article article) {
		super();
		
		this.pick_up_address = pick_up_address;		
		this.insurance = insurance;		
		this.state = state;
		this.countReviews = 0;		
		this.price = price;		
		this.description = description;		
		this.startTime = startTime;		
		this.endTime = endTime;	
		this.publicationTime = new Date();	
		this.article = article;	
		
		create();
	}
	
	public static List<Request> getAllRequests(Offer offer){
		return Request.find("byOffer",offer).fetch();
	}
}

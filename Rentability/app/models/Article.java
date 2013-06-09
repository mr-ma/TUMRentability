package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Article extends Model {
    
	public String name;
	
	public String description;
	
	@ManyToOne(optional = false)
	public User owner;
	
	@ManyToOne(optional = false)
	public Category category;

	public Article(String name, String description, User owner, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.category = category;
		create();
		
		
	}
	
	
	
	
	
}

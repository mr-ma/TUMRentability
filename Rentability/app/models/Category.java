package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Category extends Model {
	
	public String name;
	
	//not sure which jpa relationship to use here...
	public Category parent_category;

	public Category(String name, Category parent_Category) {
		super();
		this.name = name;
		this.parent_category = parent_Category;
		create();
		
	}
	
	
    
	
}

package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Category extends Model {
	
	@Required
	public String name;
	
	@OneToOne(optional = true)
	public Category parent_category;
	
	/*@OneToMany(mappedBy = "parent_category")
	public Collection<Category> sub_categories = new LinkedList<Category>();
*/
	public Category(String name, Category parent_Category) {
		super();
		this.name = name;
		this.parent_category = parent_Category;
		create();
	}
}

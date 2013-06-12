package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {

	public String first_name;
	
	public String last_name;
	
	public String email;
	
	public String phone;
	
	public String nick_name;

	public String passwordHash; //SHA-256 Hash
	
	public User(String first_name, String last_name, String nick_name, String email, String phone, String passwordHash) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.nick_name = nick_name;
		this.email = email;
		this.phone = phone;
		this.passwordHash = passwordHash;
		create();
	}
	
	
	
	
	
	
	
    
}

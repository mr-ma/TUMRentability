package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import java.security.*;

import models.*;


public class Application extends Controller {

    public static void index() {
    	render();
    }
    
    //Creating a new user
    public static void createUser(String firstName, String lastName, String eMail,
    		String phone, String nickName, String password){
    	
    	new User(firstName, lastName, eMail, phone, nickName, getHash(password,"SHA-256"));
    	index();
    }
    
    //Creation of a new Offer/Request/Article
    public static void createOffer()
    {
    	
    }
    
    //Generating a hash value using a given method for some data
    public static String getHash(String data, String hashMethod)
    {
    	MessageDigest md;
		try {
			//Setting the Hashfunction to SHA-256 (MD5 and others are available as well)
			md = MessageDigest.getInstance("SHA-256");
			
			//Setting the data --> can be done more than once
			md.update(data.getBytes());
			
			//generating the Hash
			byte[] byteData = md.digest();
			
			//generating a hex representation
			StringBuffer sb = new StringBuffer();
			
	        for (int i = 0; i < byteData.length; i++) {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        return sb.toString();
			
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
    }

}
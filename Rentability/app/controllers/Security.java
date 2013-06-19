package controllers;
 
import models.*;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
    	 User user = User.find("byEmail", username).first();
         return user != null && user.password.equals(password);
    }

    static void onDisconnected() {
    Application.index();
    }
//
//
//    static void onAuthenticated() {
//    Admin.index();
//    }


    static boolean check(String profile) {
//    if("admin".equals(profile)) {
//        return User.find("byEmail", connected()).<User>first().isAdmin;}
    	String constate= connected();
    	
    	if("user".equals(profile) &&  constate!=null) return true; 
    	
    	if ("nuser".equals(profile) && constate==null) return true;
    	
    	
    return false;
    }
}
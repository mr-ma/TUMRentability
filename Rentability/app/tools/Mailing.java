package tools;

import java.util.*;

import models.*;

import org.apache.commons.mail.*;

import play.mvc.*;

public class Mailing {
	
	final static String SENDER = "rentabilityMUC@gmail.com";
	final static String SMTP = "smtp.gmail.com";
	final static String PASSWORD = "Rentability123";
	final static int PORT = 465;

	/*After a new Request has been created a confirm mail is send to the requesting User
	and one is send to the owner.*/
	public static void newRequest(User requestingUser, Request request) {
		
		User owner = request.offer.article.owner;
		String message;
		String subject;
		
        //Send E-Mail to owner of the article
		subject = "A new request for the Article: " + request.offer.article.name + " has been received!";
		message = "<html><body><p>Dear " + owner.nick_name + "</p>" +
				"<p>Your Article: " + request.offer.article.name + " has been requested. <br />" +
				"Please log into your Rentability account for further steps. <br /></p>" +
				"<p>The Request Details:" +
				"<br />Start Time: " + request.startTime +
				"<br />End Time: " + request.endTime +
				"<br />Requesting User: " + requestingUser.nick_name + 
				"<br />Adjusted Price: " + request.adjustedPrice + "</p>" +
				"<br /><br />Your Rentability Team!</body></html>";
		sendMail(owner.email, subject, message);
			
		//Send confirmation E-Mail to the requester
		subject = "Your Request has successfully been created!";
		message = "<html><body><p>Dear " + requestingUser.nick_name + ",</p><p>" +
				"your request has successfully been created! Log into your Account for details.</p>" +
				"<p>Your Rentability Team!</p></body></html>";
		sendMail(requestingUser.email, subject, message);
	}

	//Send the confirmation Mail to the user for registration
	public static void sendConfirmationMail(User user) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("code", user.confirmationCode);
		String confirmation_link = Router.getFullUrl("Application.confirm", args);
		String subject = "Please verify your account";
		String message = "<html><body><p>Dear " + user.nick_name + "" +
				",</p><p>please verify your account by clicking the link below:</p>" +
				"<p><a href=\""+ confirmation_link + "\">" + confirmation_link + "</a></p>" +
				"<p>Thanks! Your Rentability Team!</p></body></html>";
		
		sendMail(user.email, subject, message);
	}
	
	//Technical stuff to actually send the mail
	private static void sendMail(String recipient, String subject, String htmlMessage){
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(SMTP);
			email.setSmtpPort(PORT);
			email.setAuthenticator(new DefaultAuthenticator(SENDER, PASSWORD));
			email.setSSL(true);
			email.setFrom(SENDER);
			email.setSubject(subject);
			email.addTo(recipient);
			email.setHtmlMsg(htmlMessage);
			email.send();
		} catch (EmailException emailex) {
			System.out.println(emailex.getMessage());
		}
	}
}

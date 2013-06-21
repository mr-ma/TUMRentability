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

	public static void newRequest(User requestingUser, Request request) {
		
		User owner = request.offer.article.owner;
		
        try {
        	
        	//Send E-Mail to owner of the article
			HtmlEmail email = new HtmlEmail();
			email.setHostName(SMTP);
			email.setSmtpPort(PORT);
			email.setAuthenticator(new DefaultAuthenticator(SENDER, PASSWORD));
			email.setSSL(true);
			email.setFrom(SENDER);
			email.setSubject("New Request for Article: " + request.offer.article.name);
			email.setHtmlMsg("<html><body><p>Dear User</p></body></html>");
			email.addTo(owner.email);
			email.send();
			
			//Send confirmation E-Mail to the requester
			HtmlEmail email2 = new HtmlEmail();
			email2.setHostName(SMTP);
			email2.setSmtpPort(PORT);
			email2.setAuthenticator(new DefaultAuthenticator(SENDER, PASSWORD));
			email2.setSSL(true);
			email2.setFrom(SENDER);
			email2.setSubject("Your Request has sucessfully been created!");
			email2.setHtmlMsg("<html><body><p>Dear " + requestingUser.nick_name + ",</p><p>" +
					"your request has successfully been created! Log into your Account for details.</p>" +
					"<p>Your Rentability Team!</p></body></html>");
			email2.addTo(owner.email);
			email2.send();
		} catch (EmailException e) {
			e.getMessage();
		}	
	}

	public static void sendConfirmationMail(User user) {
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("code", user.confirmationCode);
			String confirmation_link = Router.getFullUrl("Application.confirm", args);
			System.out.println(confirmation_link);
			HtmlEmail email = new HtmlEmail();
			email.setHostName(SMTP);
			email.setSmtpPort(PORT);
			email.setAuthenticator(new DefaultAuthenticator(SENDER, PASSWORD));
			email.setSSL(true);
			email.setFrom(SENDER);
			email.setSubject("Verify your Rentability account");
			email.addTo(user.email);
			email.setHtmlMsg("<html><body><p>Dear " + user.nick_name + "" +
					",</p><p>please verify your account by clicking the link below:</p>" +
					"<p><a href='"+ confirmation_link + "'>" + confirmation_link + "</a></p>" +
							"<p>Thanks! Your Rentability Team!</p></body></html>");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
}

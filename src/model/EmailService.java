package model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailService implements ServiceEE {

	private void sendEmail(String message1, String mailTo) {
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");

			String acc = "mohansary98@gmail.com";
			String pass = "xxxxxxxxxxxxxxxxx";

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Session session = Session.getInstance(properties, new Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							 return new PasswordAuthentication(acc, pass);
						}
					});
					try {
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(acc));
						message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
						message.setSubject("Clinic Appointment Alert");
						message.setText(message1);
						
						Transport.send(message);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}).run();
		} catch (Exception e) {
		}
	}

	@Override
	public void update(String message, String mailTo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendEmail(message, mailTo);
			}
		}).start();
	}
}
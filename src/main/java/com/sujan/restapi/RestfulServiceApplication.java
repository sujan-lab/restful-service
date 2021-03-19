package com.sujan.restapi;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import com.sujan.restapi.model.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;


@SpringBootApplication
@RestController
public class RestfulServiceApplication {
	@Value("${gmail.username}")
	private String username;
	@Value("${gmail.password}")
	private String password;
	public static void main(String[] args) {

		SpringApplication.run(RestfulServiceApplication.class, args);
	}
	@RequestMapping(value="/send", method = RequestMethod.POST)
	public String sendEmail(@RequestBody EmailService emailService) throws IOException, MessagingException {
		sendMail(emailService);
		return "Email Sent Successfully";
	}
	private void sendMail(EmailService emailService) throws MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(username, false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailService.getTo_address()));
		msg.setSubject(emailService.getSubject());
		msg.setContent(emailService.getBody(), "text/html");
		msg.setSentDate(new Date());
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailService.getBody(), "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		MimeBodyPart attachPart = new MimeBodyPart();

		attachPart.attachFile("C:\\Users\\sujan\\Pictures\\suhi_2nd\\Test.jpg");

		multipart.addBodyPart(attachPart);
		msg.setContent(multipart);
		// sends the e-mail
		Transport.send(msg);

	}
}

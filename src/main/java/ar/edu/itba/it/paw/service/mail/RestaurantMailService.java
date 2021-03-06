package ar.edu.itba.it.paw.service.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.service.exception.MailConfigurationException;

@Component
public class RestaurantMailService implements MailService {

	private static String FROM = "noreply.oleosguide@gmail.com";
	private static String ERROR_TO = "lmoscovicz@gmail.com";

	@Override
	public void sendRecoveryMail(User user, HttpServletRequest request) {
		String token = RandomStringUtils.randomAlphabetic(20);
		user.setToken(token);

		String url = "http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath()
				+ "/bin/resetPassword?token=" + user.getToken();
		String content = "Click <a href=\"" + url
				+ "\">here</a> to reset your password.";

		try {
			send(FROM, user.getEmail(), "Recover your password", content,
					"text/html");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MailConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void sendErrorMail(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		try {
			send(FROM, ERROR_TO, "Exception at Oleo's", sw.toString(),
					"text/html");
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MailConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void send(String from, String to, String subject, String content,
			String contentType) throws AddressException, MessagingException,
			MailConfigurationException {
		final Properties props = new Properties();
		try {
			InputStream propsIs = getClass().getClassLoader()
					.getResourceAsStream("mailer.properties");
			props.load(propsIs);
		} catch (IOException ex) {
			throw new MailConfigurationException();
		}

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("user"),
						props.getProperty("pass"));
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		InternetAddress toInetAddr = new InternetAddress(to);
		message.addRecipient(Message.RecipientType.TO, toInetAddr);
		message.setSubject(subject);
		message.setContent(content, contentType);
		Transport.send(message, message.getAllRecipients());
	}

}

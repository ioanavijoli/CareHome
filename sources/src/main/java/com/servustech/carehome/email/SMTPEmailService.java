/**
 *
 */
package com.servustech.carehome.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Andrei Groza
 *
 */
@Service
public class SMTPEmailService implements EmailService {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(SMTPEmailService.class);

	@Autowired
	private JavaMailSender		mailSender;

	@Override
	public void sendEmail(
			final String from,
			final String destination,
			final String subject,
			final String message) {
		doSendEmail(from, destination, subject, message);
	}

	private void doSendEmail(
			final String from,
			final String destination,
			final String subject,
			final String message) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(from);
		mailMessage.setTo(destination);
		mailMessage.setText(message);
		mailMessage.setSubject(subject);
		try {
			this.mailSender.send(mailMessage);
		} catch (final MailException e) {
			LOGGER.error("Error sending email to " + destination, e);
		}
	}

}

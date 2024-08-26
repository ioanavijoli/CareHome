/**
 *
 */
package com.servustech.carehome.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.servustech.carehome.persistence.model.User;

/**
 * @author Andrei Groza
 *
 */
@Service
public class MobAllEmailService {

	@Value("${mail.from}")
	private String				mailFrom;

	private final EmailService	emailService;

	@Autowired
	public MobAllEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	public void sendWelcomeMessage(
			final User user,
			final String password) {
		this.emailService.sendEmail(this.mailFrom, user.getEmail(), "Welcome to MobAll!", "Welcome to MobAll! Your password is " + password);
	}

}

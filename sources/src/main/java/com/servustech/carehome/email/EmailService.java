package com.servustech.carehome.email;

public interface EmailService {

	/**
	 * Send email
	 *
	 * @param destination
	 *            the destination email address
	 * @param subject
	 *            the subject of the email
	 * @param message
	 *            the content of the email
	 */
	void sendEmail(
			String from,
			String destination,
			String subject,
			String message);

}

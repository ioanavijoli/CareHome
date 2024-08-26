/**
 *
 */
package com.servustech.carehome.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Andrei Groza
 *
 */
@Configuration
public class SMTPEmailConfig {
	@Autowired
	private Environment env;

	@Bean
	public JavaMailSender javaMailSender() {
		final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(this.env.getProperty("mail.server.host"));
		javaMailSender.setPort(Integer.parseInt(this.env.getProperty("mail.server.port")));
		javaMailSender.setUsername(this.env.getProperty("mail.server.user"));
		javaMailSender.setPassword(this.env.getProperty("mail.server.password"));

		return javaMailSender;
	}
}

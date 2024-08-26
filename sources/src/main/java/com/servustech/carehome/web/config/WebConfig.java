/**
 *
 */
package com.servustech.carehome.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Andrei Groza
 *
 */
@Configuration
public class WebConfig {
	// @Bean
	// public LocaleResolver localeResolver() {
	// return new FixedLocaleResolver();
	// }

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setCacheSeconds(0);

		return messageSource;
	}
}

package com.servustech.carehome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author Andrei Groza
 *
 */
@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CareHomeWarApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			final SpringApplicationBuilder application) {
		return application.sources(CareHomeWarApplication.class);
	}

	public static void main(
			final String[] args) throws Exception {
		SpringApplication.run(CareHomeWarApplication.class, args);
	}

}
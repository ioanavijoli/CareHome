package com.servustech.carehome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
// @EnableMongoRepositories("your_package_repositoy_name")
// @EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration extends AbstractMongoConfiguration {

	// @Value("${spring.data.mongodb.host}")
	// private String host;
	//
	// @Value("${spring.data.mongodb.port}")
	// private Integer port;
	//
	// @Value("${spring.data.mongodb.username}")
	// private String username;
	//
	// @Value("${spring.data.mongodb.database}")
	// private String database;
	//
	// @Value("${spring.data.mongodb.password}")
	// private String password;

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Override
	public String getDatabaseName() {
		return "carehomedb";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient("localhost", 27017);
	}

	@Override
	public MongoDbFactory mongoDbFactory() throws Exception {
		final MongoClient mongoClient = new MongoClient("localhost", 27017);
		return new SimpleMongoDbFactory(mongoClient, "carehomedb");
	}

}
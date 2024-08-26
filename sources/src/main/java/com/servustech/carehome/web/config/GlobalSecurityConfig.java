package com.servustech.carehome.web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/*
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class GlobalSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
	private TokenAuthenticationProvider tokenAuthenticationProvider;

	@Override
	protected AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(this.tokenAuthenticationProvider));
	}
}

 */
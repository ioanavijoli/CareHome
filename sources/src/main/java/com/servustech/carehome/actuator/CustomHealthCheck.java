package com.servustech.carehome.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

//@Component
public class CustomHealthCheck implements HealthIndicator {
	@Override
	public Health health() {
		final int errorCode = 0;
		if (errorCode != 1) {
			return Health.down().withDetail("Error Code", errorCode).build();
		}
		return Health.up().build();
	}

}

package com.servustech.carehome.actuator;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

//@Component
public class MobAllHealthCheck extends AbstractHealthIndicator {
	@Override
	protected void doHealthCheck(
			final Health.Builder bldr) throws Exception {
		// TODO implement some check
		final boolean running = true;
		if (running) {
			bldr.up();
		} else {
			bldr.down();
		}
	}
}
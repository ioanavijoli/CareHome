package com.servustech.carehome.web.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.ErrorResponse;

public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationEntryPoint.class);

	@Override
	public void commence(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final AuthenticationException authException) throws IOException {
		LOGGER.error("Authentication exception", authException);
		// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// Get the printwriter object from response to write the required json object to the output stream
		final PrintWriter out = response.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will return your json object
		String errorCode = "access.denied";
		if (authException instanceof CredentialsExpiredException) {
			errorCode = "token.expired";
		}
		out.print(new ObjectMapper().writeValueAsString(
				new ErrorResponse<RestAPIError>(new RestAPIError(RestAPIError.Type.AUTHENTICATION, errorCode, authException.getLocalizedMessage(), null))));
		out.flush();
	}

}
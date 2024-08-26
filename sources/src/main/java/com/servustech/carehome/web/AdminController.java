/**
 *
 */
package com.servustech.carehome.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.servustech.carehome.util.AppConstants;

/**
 * @author Andrei Groza
 *
 */
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/admin")
public class AdminController {
	/** Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	/**
	 * Ping admin service
	 *
	 * @param request
	 *            the http request
	 * @param response
	 *            the ping response, in a valid html format
	 * @throws IOException
	 *             if error occurs
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public void ping(
			final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		LOGGER.debug("Pinging Admin Controller service from IP {}:{}!", request.getRemoteHost(), request.getRemotePort());

		response.setContentType("text/html");
		response.getOutputStream().print("<h1>AdminController service is up and running!</h1>");
	}
}

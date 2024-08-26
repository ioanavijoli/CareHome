/**
 *
 */
package com.servustech.carehome.util;

/**
 * Application constants
 *
 * @author Andrei Groza
 *
 */
public class AppConstants {

	public static final String	API_V1_PREFIX					= "/api/v1";

	public static final String	DATE_FORMAT						= "dd/MM/yyyy";
	public static final String	DATE_TIME_FORMAT				= "dd/MM/yyyy HH:mm:ss";

	public static final String	PAGINATION_PAGE_NUMBER			= "page";
	public static final String	PAGINATION_DEFAULT_PAGE_NUMBER	= "1";
	public static final String	PAGINATION_PAGE_SIZE			= "pageSize";
	public static final String	PAGINATION_DEFAULT_SIZE			= "30";

	private AppConstants() {
		throw new UnsupportedOperationException("Cannot instantiate this class");
	}
}

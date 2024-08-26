/**
 *
 */
package com.servustech.carehome.util;

/**
 * Persistence constants. All collections and document fields are defined here.
 *
 * @author Andrei Groza
 *
 */
public class PersistenceConstants {

	public static final String USER_COLLECTION = "users";
	public static final String TOKEN_COLLECTION = "tokens";
	public static final String CODE_COLLECTION = "codes";
	public static final String SEQUENCE_COLLECTION = "sequences";
	public static final String FILE_RESOURCE_COLLECTION = "files";
	public static final String USER_PROFILE_COLLECTION = "userProfiles";
	public static final String BUSINESS_COLLECTION = "businesses";
	public static final String POINT_OF_INTEREST_COLLECTION = "pointOfInterest";
	public static final String COMMENT_COLLECTION = "comments";

	private PersistenceConstants() {
		throw new UnsupportedOperationException("Cannot instantiate this class");
	}
}

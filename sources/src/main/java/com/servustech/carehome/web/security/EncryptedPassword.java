/**
 *
 */
package com.servustech.carehome.web.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author Andrei Groza
 *
 */
public class EncryptedPassword {
	private static final String	PEPPER	= "wDNX08We1GkEjqEM5JPbWeDUEOmUWvo5FPA9oEAS3pYwMYIDHtb6h";

	private final String		password;
	private final String		salt;

	/**
	 * @param password
	 *            the password in plain text
	 */
	public EncryptedPassword(final String password) {
		this.password = PEPPER + password;
		this.salt = BCrypt.gensalt();

	}

	public String hash() {
		return BCrypt.hashpw(this.password, this.salt);
	}

	public boolean match(
			final String hash) {
		return BCrypt.checkpw(this.password, hash);
	}

}

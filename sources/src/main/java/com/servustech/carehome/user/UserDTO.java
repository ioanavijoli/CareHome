/**
 *
 */
package com.servustech.carehome.user;

import java.util.ArrayList;
import java.util.Collection;

import com.servustech.carehome.web.model.BaseDTO;

/**
 * User DTO model
 *
 * @author Andrei Groza
 *
 */
public class UserDTO extends BaseDTO {
	private String				username;
	private String				email;
	private String				password;
	private String				role;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(
			final String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(
			final String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(
			final String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDTO [username=" + this.username + ", email=" + this.email + ", role =" + this.role + ", getUUID()=" + getUUID() + "]";
	}

}

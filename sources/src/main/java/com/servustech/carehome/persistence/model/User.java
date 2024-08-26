/**
 *
 */
package com.servustech.carehome.persistence.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.AuditedEntity;

/**
 * @author Andrei Groza
 *
 */
@Document(collection = PersistenceConstants.USER_COLLECTION)
@Exposable
public class User extends AuditedEntity {
	/**
	 *
	 * @author Andrei Groza
	 *
	 */
	public enum Role {
		/**
		 * The guy which will monitor app healthy. @See actuator endpoints.
		 */
		ADMIN,

		/**
		 * The normal user in the system. The guy who drinks cofee.
		 */
		USER,

		/**
		 *
		 */
		BUSINESS
	}

	/** Username */
	@NotBlank
	@Length(max = 100)
	private String				username;

	/** Email */
	@NotBlank
	@Email
	@Length(max = 100)
	private String				email;

	/** Password */
	@Length(max = 500)
	@NotBlank
	private String				password;

	/** Password salt */

	private String				salt;

	@NotNull
	private Role				role;

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

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return this.salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(
			final String salt) {
		this.salt = salt;
	}

	/** Roles */
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}

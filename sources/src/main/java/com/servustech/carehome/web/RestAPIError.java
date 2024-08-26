/**
 *
 */
package com.servustech.carehome.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Andrei Groza
 *
 */
@JsonInclude(Include.NON_NULL)
public final class RestAPIError {
	public enum Type {
		AUTHENTICATION, AUTHORIZATION, VALIDATION, PERSISTENCE, BUSINESS, GENERAL
	}

	private Type	type;
	private String	code;
	private String	message;
	private String	details;

	/**
	 * Default constructor
	 */
	public RestAPIError() {
	}

	/**
	 * Parameterized constructor
	 *
	 * @param type
	 * @param code
	 * @param message
	 * @param details
	 */
	public RestAPIError(final Type type, final String code, final String message, final String details) {
		super();
		this.type = type;
		this.code = code;
		this.message = message;
		this.details = details;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(
			final Type type) {
		this.type = type;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(
			final String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(
			final String message) {
		this.message = message;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return this.details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(
			final String details) {
		this.details = details;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
		result = prime * result + ((this.details == null) ? 0 : this.details.hashCode());
		result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(
			final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RestAPIError other = (RestAPIError) obj;
		if (this.code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!this.code.equals(other.code)) {
			return false;
		}
		if (this.details == null) {
			if (other.details != null) {
				return false;
			}
		} else if (!this.details.equals(other.details)) {
			return false;
		}
		if (this.message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!this.message.equals(other.message)) {
			return false;
		}
		if (this.type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RestAPIError [type=" + this.type + ", code=" + this.code + ", message=" + this.message + ", details=" + this.details + "]";
	}

}

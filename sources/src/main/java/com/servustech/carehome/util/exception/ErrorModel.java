/**
 *
 */
package com.servustech.carehome.util.exception;

/**
 * @author Andrei Groza
 *
 */
public final class ErrorModel {
	private final String	code;
	private final String	message;
	private final String	details;

	/**
	 * @param code
	 */
	public ErrorModel(final String code) {
		this(code, null, null);
	}

	/**
	 * @param code
	 * @param message
	 */
	public ErrorModel(final String code, final String message) {
		this(code, message, null);
	}

	/**
	 * @param code
	 * @param message
	 * @param details
	 */
	public ErrorModel(final String code, final String message, final String details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}

	/**
	 * @return the code
	 */
	public String code() {
		return this.code;
	}

	/**
	 * @return the message
	 */
	public String message() {
		return this.message;
	}

	/**
	 * @return the details
	 */
	public String details() {
		return this.details;
	}

}

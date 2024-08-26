/**
 *
 */
package com.servustech.carehome.util.exception;

/**
 * @author Andrei Groza
 *
 */
public final class ErrorMessage {
	private final String	code;
	private final String	defaultValue;

	/**
	 * @param code
	 */
	public ErrorMessage(final String code) {
		this(code, null);
	}

	/**
	 * @param code
	 * @param defaultValue
	 */
	public ErrorMessage(final String code, final String defaultValue) {
		this.code = code;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

}

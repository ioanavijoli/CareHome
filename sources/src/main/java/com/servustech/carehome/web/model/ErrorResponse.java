/**
 *
 */
package com.servustech.carehome.web.model;

/**
 * @author Andrei Groza
 *
 */
public final class ErrorResponse<T> {
	private T error;

	/**
	 * @param error
	 */
	public ErrorResponse(final T error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public T getError() {
		return this.error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(
			final T error) {
		this.error = error;
	}

}

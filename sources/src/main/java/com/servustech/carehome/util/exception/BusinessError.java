/**
 *
 */
package com.servustech.carehome.util.exception;

import java.util.Optional;

/**
 * @author Andrei Groza
 *
 */
public class BusinessError extends RuntimeException {
	private static final long		serialVersionUID	= 8647726307259361229L;

	private final Optional<ErrorMessage>	message;

	/**
	 *
	 * @param message
	 */
	public BusinessError(final ErrorMessage message) {
		super(message.getCode());
		this.message = Optional.of(message);
	}

	/**
	 * @param message
	 */
	public BusinessError(final String message) {
		this(message, null);
	}

	/**
	 * @param cause
	 */
	public BusinessError(final Throwable cause) {
		this(null, cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BusinessError(final String message, final Throwable cause) {
		super(message, cause);
		this.message = Optional.empty();
	}

	/**
	 * Retrieves message associated with this exception
	 *
	 * @return
	 */
	public Optional<ErrorMessage> message() {
		return this.message;
	}

}

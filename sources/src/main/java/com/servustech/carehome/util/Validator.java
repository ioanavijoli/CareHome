/**
 *
 */
package com.servustech.carehome.util;

import com.servustech.carehome.util.exception.ValidationError;

/**
 * @author Andrei Groza
 *
 */
public interface Validator<T> {
	/**
	 * Validates the model. If validation fails, a {@link ValidationError} is thrown
	 *
	 * @param model
	 *            the model to validate
	 */
	void validate(
			T model);
}

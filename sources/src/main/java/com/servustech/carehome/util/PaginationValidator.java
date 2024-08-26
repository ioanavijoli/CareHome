/**
 *
 */
package com.servustech.carehome.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;

/**
 * Pagination validator
 *
 * @author Andrei Groza
 *
 */
@Component
public class PaginationValidator {

	/**
	 * Validate pagination parameters. If validation fails, a {@link ValidationError} is thrown.
	 *
	 * @param page
	 *            the page number
	 * @param size
	 *            the page size
	 */
	public void validate(
			final int page,
			final int size) {
		final List<ErrorModel> errors = new ArrayList<>();

		if (page < 1 || page > Integer.MAX_VALUE) {
			errors.add(new ErrorModel("pagination.page.range"));
		}

		if (size < 1 || size > Integer.MAX_VALUE) {
			errors.add(new ErrorModel("pagination.size.range"));
		}

		if (!errors.isEmpty()) {
			throw new ValidationError(errors);
		}
	}

}

/**
 *
 */
package com.servustech.carehome.util.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrei Groza
 *
 */
public class ValidationError extends RuntimeException {
	private static final long		serialVersionUID	= 446227856311916291L;

	private final List<ErrorModel>	errorModels;

	/**
	 * @param errorObjects
	 */
	public ValidationError(final List<ErrorModel> errorObjects) {
		this.errorModels = new ArrayList<>(errorObjects);
	}

	/**
	 * @return the errorObjects
	 */
	public List<ErrorModel> errorModels() {
		return Collections.unmodifiableList(this.errorModels);
	}

}

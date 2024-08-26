/**
 *
 */
package com.servustech.carehome.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Andrei Groza
 *
 */
@JsonInclude(Include.NON_NULL)
public class BaseDTO {
	public static final BaseDTO	EMPTY	= new BaseDTO();

	private String				UUID;

	/**
	 * @return the UUID
	 */
	public String getUUID() {
		return this.UUID;
	}

	/**
	 * @param UUID
	 *            the UUID to set
	 */
	public void setUUID(
			final String UUID) {
		this.UUID = UUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.UUID == null) ? 0 : this.UUID.hashCode());
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
		final BaseDTO other = (BaseDTO) obj;
		if (this.UUID == null) {
			if (other.UUID != null) {
				return false;
			}
		} else if (!this.UUID.equals(other.UUID)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BaseDTO [UUID=" + this.UUID + "]";
	}

}

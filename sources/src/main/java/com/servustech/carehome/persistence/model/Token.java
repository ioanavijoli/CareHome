/**
 *
 */
package com.servustech.carehome.persistence.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.AuditedEntity;

/**
 * @author Andrei Groza
 *
 */
@Document(collection = PersistenceConstants.TOKEN_COLLECTION)
@Exposable
public class Token extends AuditedEntity {
	private ObjectId		userID;
	private String			value;
	private LocalDateTime	expirationTime;

	/**
	 * @return the userID
	 */
	public ObjectId getUserID() {
		return this.userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(
			final ObjectId userID) {
		this.userID = userID;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(
			final String value) {
		this.value = value;
	}

	/**
	 * @return the expirationTime
	 */
	public LocalDateTime getExpirationTime() {
		return this.expirationTime;
	}

	/**
	 * @param expirationTime
	 *            the expirationTime to set
	 */
	public void setExpirationTime(
			final LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

}

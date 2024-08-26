package com.servustech.carehome.persistence.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;

@Document(collection = PersistenceConstants.SEQUENCE_COLLECTION)
@Exposable
public class Sequence {
	@Id
	private ObjectId	id;

	/**
	 * ID sequence
	 */
	private long		idSequence;

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(
			final ObjectId id) {
		this.id = id;
	}

	/**
	 * @return the idSequence
	 */
	public long getIdSequence() {
		return this.idSequence;
	}

	/**
	 * @param idSequence
	 *            the idSequence to set
	 */
	public void setIdSequence(
			final long idSequence) {
		this.idSequence = idSequence;
	}

}
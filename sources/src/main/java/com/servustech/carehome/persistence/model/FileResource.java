package com.servustech.carehome.persistence.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.AuditedEntity;

/**
 *
 * @author Eniko Zsido
 *
 *
 */

@Document(collection = PersistenceConstants.FILE_RESOURCE_COLLECTION)
@Exposable
public class FileResource extends AuditedEntity {
	private String			userUUID;
	private String			name;
	private String			description;
	private String path;
	private String			contentType;
	private LocalDateTime	uploadTime;

	/**
	 * @return the userUUID
	 */
	public String getUserUUID() {
		return this.userUUID;
	}

	/**
	 * @param userUUID
	 *            the userUUID to set
	 */
	public void setUserUUID(
			final String userUUID) {
		this.userUUID = userUUID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(
			final String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(
			final String description) {
		this.description = description;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(
			final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the uploadTime
	 */
	public LocalDateTime getUploadTime() {
		return this.uploadTime;
	}

	/**
	 * @param uploadTime
	 *            the uploadTime to set
	 */
	public void setUploadTime(
			final LocalDateTime uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}

package com.servustech.carehome.file;

import java.util.Arrays;

import com.servustech.carehome.web.model.BaseDTO;

/**
 * File DTO model
 *
 * @author Andrei Groza
 *
 */
public class FileResourceDTO extends BaseDTO {
	private String	userUUID;
	private String	name;
	private String	description;
	private byte[]	content;
	private String	contentType;
	private String	uploadTime;
	private String originalName;
	private String path;

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
	 * @return the content
	 */
	public byte[] getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(
			final byte[] content) {
		this.content = content;
	}

	/**
	 * @return the uploadTime
	 */
	public String getUploadTime() {
		return this.uploadTime;
	}

	/**
	 * @param uploadTime
	 *            the uploadTime to set
	 */
	public void setUploadTime(
			final String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "FileResourceDTO [userUUID=" + this.userUUID + ", name=" + this.name + ", description=" + this.description + ", content="
				+ Arrays.toString(this.content) + ", contentType=" + this.contentType + ", uploadTime=" + this.uploadTime + "]";
	}
}

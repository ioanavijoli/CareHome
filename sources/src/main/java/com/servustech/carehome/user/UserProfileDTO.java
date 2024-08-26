package com.servustech.carehome.user;

import com.servustech.carehome.web.model.BaseDTO;

import java.time.LocalDate;

public class UserProfileDTO extends BaseDTO {

	private String userUUID;
	private String firstname;
	private String surname;
	private String religion;
	private String gender;
	private LocalDate birthdate;
	private byte[] avatar;
	private String avatarContentType;
	private String relationship;
	private byte[] smallAvatar;

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public String getAvatarContentType() {
		return avatarContentType;
	}

	public void setAvatarContentType(String avatarContentType) {
		this.avatarContentType = avatarContentType;
	}


	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public byte[] getSmallAvatar() {
		return smallAvatar;
	}

	public void setSmallAvatar(byte[] smallAvatar) {
		this.smallAvatar = smallAvatar;
	}
}

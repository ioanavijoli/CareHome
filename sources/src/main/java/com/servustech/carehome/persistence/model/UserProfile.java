package com.servustech.carehome.persistence.model;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.AuditedEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = PersistenceConstants.USER_PROFILE_COLLECTION)
@Exposable
public class UserProfile extends AuditedEntity {

	private String userUUID;
	private String firstname;
	private String surname;	
	private LocalDate birthdate;
	private byte[] avatar;
	@Length(max = 100)
	private String religion;
	@Length(max = 20)
	private String gender;
	private String avatarContentType;
	private String relationship;
	private byte[] smallAvatar;

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
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
	
	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
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

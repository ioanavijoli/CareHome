package com.servustech.carehome.persistence.model;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.poi.Service;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.Address;
import com.servustech.mongo.utils.model.AuditedEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Document(collection = PersistenceConstants.POINT_OF_INTEREST_COLLECTION)
@Exposable
public class PointOfInterest extends AuditedEntity {

	@NotBlank
	@Length(max = 200)
	private String name;

	@NotBlank
	@Email
	@Length(max = 100)
	private String email;

	@NotBlank
	@Length(max = 2000)
	private String description;

	@NotBlank
	@Length(max = 20)
	private String phoneNumber;

	@Length(max = 100)
	private String contactPerson;

	@NotBlank
	private String businessUUID;

//	@NotBlank
//	@Min(1)
//	@Max(5)
	private int price;

	private List<Service> services;

	/**
	 * Address field
	 */
	private POIAddress address;

	private byte[] firstPicture;

	/**
	 * A list of UUIDs that represent pictures
	 */
	private List<String> pictures;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public POIAddress getAddress() {
		return address;
	}

	public void setAddress(POIAddress address) {
		this.address = address;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}


	public String getBusinessUUID() {
		return businessUUID;
	}

	public void setBusinessUUID(String businessUUID) {
		this.businessUUID = businessUUID;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public byte[] getFirstPicture() {
		return firstPicture;
	}

	public void setFirstPicture(byte[] firstPicture) {
		this.firstPicture = firstPicture;
	}
}

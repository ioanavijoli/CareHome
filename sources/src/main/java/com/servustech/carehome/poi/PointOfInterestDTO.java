package com.servustech.carehome.poi;

import com.servustech.carehome.persistence.model.POIAddress;
import com.servustech.carehome.web.model.BaseDTO;
import com.servustech.mongo.utils.model.Address;

import java.util.List;

public class PointOfInterestDTO extends BaseDTO {

	private String name;
	private String email;
	private String description;
	private String phoneNumber;
	private String contactPerson;
	private POIAddress address;
	private String businessUUID;
	private List<String> pictures;
	private List<Service> services;
	private double rating;
	private double priceRating;
	private int price;
	private byte[] firstPicture;

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

	public String getBusinessUUID() {
		return businessUUID;
	}

	public void setBusinessUUID(String businessUUID) {
		this.businessUUID = businessUUID;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getPriceRating() {
		return priceRating;
	}

	public void setPriceRating(double priceRating) {
		this.priceRating = priceRating;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}

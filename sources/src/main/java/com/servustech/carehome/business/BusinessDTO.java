package com.servustech.carehome.business;

import com.servustech.carehome.web.model.BaseDTO;
import com.servustech.mongo.utils.model.Address;

import java.util.List;

public class BusinessDTO extends BaseDTO {

    private String userUUID;
    private String name;
    private String website;
    private String services;
    private byte[] logo;
    private Integer carehomes;

    /**
     * Address field
     */
    private Address address;
    /**
     * A list of UUIDs that represent pictures
     */
    private List<String> pictures;


    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Integer getCarehomes() {
        return carehomes;
    }

    public void setCarehomes(Integer carehomes) {
        this.carehomes = carehomes;
    }
}

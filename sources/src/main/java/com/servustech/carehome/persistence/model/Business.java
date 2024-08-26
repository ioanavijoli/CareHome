package com.servustech.carehome.persistence.model;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.Address;
import com.servustech.mongo.utils.model.AuditedEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = PersistenceConstants.BUSINESS_COLLECTION)
@Exposable
public class Business extends AuditedEntity{

    @NotBlank
    private String userUUID;
    @NotBlank
    private String name;
    private String website;
    private String services;
    private byte[] logo;
    private String logoContentType;

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

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }
}

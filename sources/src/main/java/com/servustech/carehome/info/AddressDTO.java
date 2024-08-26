package com.servustech.carehome.info;

import org.springframework.data.mongodb.core.mapping.Field;

public class AddressDTO {
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String street;
    private String number;
    private String county;


    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

package com.servustech.carehome.info;

import org.springframework.data.mongodb.core.mapping.Field;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "address")
public class Address {
    @Field("Country")
    private String country;

    @Field("State")
    private String state;

    @Field("City")
    private String city;

    @Field("PostalCode")
    private String postalCode;

    @Field("Street")
    private String street;

    @Field("County")
    private String county;

    @Field("Number")
    private String number;

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

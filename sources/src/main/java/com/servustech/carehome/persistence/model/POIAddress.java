package com.servustech.carehome.persistence.model;

import com.servustech.mongo.utils.model.Address;

/**
 * Created by Dinulet on 12/16/2016.
 */
public class POIAddress extends Address {
    private String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}


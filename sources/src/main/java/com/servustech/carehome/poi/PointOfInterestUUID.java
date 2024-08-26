package com.servustech.carehome.poi;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dinulet on 12/19/2016.
 */
public class PointOfInterestUUID {
    List<String> uuids;

    public PointOfInterestUUID() {
        uuids = new LinkedList<>();
    }

    public List<String> getUuids() {
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }
}

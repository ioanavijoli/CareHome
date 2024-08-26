package com.servustech.carehome.google.maps;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ServiceException;
import com.servustech.carehome.util.exception.ValidationError;
import com.servustech.mongo.utils.exception.EntityNotFoundException;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GeoCodingService {

    private final GeoApiContext geoApiContext;

    public GeoCodingService() {
        this.geoApiContext = new GeoApiContext().setApiKey("AIzaSyARU1uwuffzZ_5jtFKmi2RyvlIHFSLcA8g");
    }

    public GeoJsonPoint geoCode(String address) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, address).await();

            if (results.length != 1) {
                throw new EntityNotFoundException("address.invalid");
            }

            double latitude = results[0].geometry.location.lat;
            double longitude = results[0].geometry.location.lng;

            GeoJsonPoint point = new GeoJsonPoint(latitude, longitude);
            return point;
        } catch (Exception e) {
            if (e.getMessage().equals("address.invalid")) {
                throw new ValidationError(Arrays.asList(new ErrorModel("address.invalid")));
            } else {
                throw new ServiceException("Google maps not reacheable");
            }
        }

    }

}

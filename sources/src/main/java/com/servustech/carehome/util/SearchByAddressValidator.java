package com.servustech.carehome.util;

import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by IDinu on 12/16/2016.
 */
@Component
public class SearchByAddressValidator {
    Predicate<List> isEmpty = CollectionUtils::isEmpty;

    public void validate(Integer zipCode, Integer zipCodeRange,
                         String country, String city, String street) {
        final List<ErrorModel> errors = new ArrayList<>();

        if (Objects.nonNull(zipCode) && Objects.isNull(zipCodeRange)
                || Objects.isNull(zipCode) && Objects.nonNull(zipCodeRange)) {
            errors.add(new ErrorModel("zipcode.search.data.invalid"));
        }

        if (Objects.isNull(country) && Objects.isNull(city) && Objects.isNull(street) && Objects.isNull(zipCode)) {
            errors.add(new ErrorModel("address.search.data.invalid"));
        }

        if (!isEmpty.test(errors)) {
            throw new ValidationError(errors);
        }

    }

    public void validate(String postalCode) {
        Predicate<String> isBlank = StringUtils::isBlank;

        final List<ErrorModel> errors = new ArrayList<>();
        if(isBlank.test(postalCode)) {
            errors.add(new ErrorModel("address.empty.postalcode"));
        }

        if (!isEmpty.test(errors)) {
            throw new ValidationError(errors);
        }
    }
}

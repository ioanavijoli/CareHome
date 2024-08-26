package com.servustech.carehome.address;

import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import com.servustech.mongo.utils.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


@Component
public class AddressValidator implements Validator<Address> {

    private static final int MAX_LENGTH = 100;

    @Override
    public void validate(Address address) {
        Predicate<String> isBlank = StringUtils::isBlank;
        Predicate<List> isEmpty = CollectionUtils::isEmpty;

        final List<ErrorModel> errors = new ArrayList<>();
        if (Objects.isNull(address)) {
            errors.add(new ErrorModel("address.not.empty"));
            throw new ValidationError(errors);
        }

        if (isBlank.test(address.getCountry())){
            errors.add(new ErrorModel("address.country.not.empty"));
        } else if (address.getCountry().length() > MAX_LENGTH){
            errors.add(new ErrorModel("address.country.max.length"));
        }

        if (isBlank.test(address.getCity())){
            errors.add(new ErrorModel("address.city.not.empty"));
        } else if (address.getCity().length() > MAX_LENGTH){
            errors.add(new ErrorModel("address.city.max.length"));
        }

        if (isBlank.test(address.getStreet())){
            errors.add(new ErrorModel("address.street.not.empty"));
        } else if (address.getStreet().length() > MAX_LENGTH){
            errors.add(new ErrorModel("address.street.max.length"));
        }

        if (isBlank.test(address.getNumber())){
            errors.add(new ErrorModel("address.number.not.empty"));
        } else if (address.getNumber().length() > MAX_LENGTH) {
            errors.add(new ErrorModel("address.number.max.length"));
        }

        if (!isEmpty.test(errors)) {
            throw new ValidationError(errors);
        }

    }
}

package com.servustech.carehome.business;

import com.servustech.carehome.address.AddressValidator;
import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessValidator implements Validator<BusinessDTO> {

    private final AddressValidator addressValidator;

    @Autowired
    public BusinessValidator(AddressValidator addressValidator) {
        this.addressValidator = addressValidator;
    }

    //TODO : only name validation for now, validation for other fields only after specifications are provided
    @Override
    public void validate(BusinessDTO bus) {

        final List<ErrorModel> errors = new ArrayList<>();

        if (StringUtils.isBlank(bus.getName())) {
            errors.add(new ErrorModel("business.name.not.blank"));
        }

        // validate the address fields
        addressValidator.validate(bus.getAddress());

        if (!errors.isEmpty()) {
            throw new ValidationError(errors);
        }
    }
}

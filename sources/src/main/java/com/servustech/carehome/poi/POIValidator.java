package com.servustech.carehome.poi;

import com.servustech.carehome.address.AddressValidator;
import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class POIValidator implements Validator<PointOfInterestDTO> {

	private final AddressValidator addressValidator;

	@Autowired
	public POIValidator(AddressValidator addressValidator) {
		this.addressValidator = addressValidator;
	}

	@Override
	public void validate(PointOfInterestDTO dto) {
		final List<ErrorModel> errors = new ArrayList<>();

		// check name validity
		if (StringUtils.isBlank(dto.getName())) {
			errors.add(new ErrorModel("poi.name.not.blank"));
		} else if (StringUtils.isNotBlank(dto.getName()) && dto.getName().length() > 200) {
			errors.add(new ErrorModel("poi.name.max.length"));
		}

		// check email validity
		if (StringUtils.isBlank(dto.getEmail())) {
			errors.add(new ErrorModel("poi.email.not.blank"));
		} else {
			if (dto.getEmail().length() > 100) {
				errors.add(new ErrorModel("poi.email.max.length"));
			}
			if (!new EmailValidator().isValid(dto.getEmail(), null)) {
				errors.add(new ErrorModel("poi.email.invalid.pattern"));
			}
		}

		// check description validity
		if (StringUtils.isBlank(dto.getDescription())) {
			errors.add(new ErrorModel("poi.description.not.blank"));
		} else if (dto.getDescription().length() > 2000) {
			errors.add(new ErrorModel("poi.description.max.length"));
		}

		// check phone number validity
		if (StringUtils.isBlank(dto.getPhoneNumber())) {
			errors.add(new ErrorModel("poi.phone.not.blank"));
		} else if (dto.getPhoneNumber().length() > 20) {
			errors.add(new ErrorModel("poi.phone.max.length"));
		}

		// check contact person validity
		if (StringUtils.isNotBlank(dto.getContactPerson()) && dto.getContactPerson().length() > 100) {
			errors.add(new ErrorModel("poi.contact.max.length"));
		}

		// validate the address fields
		addressValidator.validate(dto.getAddress());

		if (!errors.isEmpty()) {
			throw new ValidationError(errors);
		}

	}

}

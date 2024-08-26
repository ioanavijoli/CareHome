package com.servustech.carehome.user;

import java.util.ArrayList;
import java.util.List;

import com.servustech.carehome.persistence.model.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.stereotype.Component;

import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;

/**
 * User validator
 *
 * @author Andrei Groza
 *
 */
@Component
public class UserValidator implements Validator<UserDTO> {

	@Override
	public void validate(
			final UserDTO userDTO) {
		final List<ErrorModel> errors = new ArrayList<>();

		if (StringUtils.isBlank(userDTO.getUsername()) && StringUtils.isBlank(userDTO.getEmail())) {
			errors.add(new ErrorModel("user.username.or.email.not.blank"));
		} else {
			if (StringUtils.isNotBlank(userDTO.getUsername())) {
				if (userDTO.getUsername().length() > 100) {
					errors.add(new ErrorModel("user.username.max.length"));
				}
			}
			if (StringUtils.isNotBlank(userDTO.getEmail())) {
				if (userDTO.getEmail().length() > 100) {
					errors.add(new ErrorModel("user.email.max.length"));
				}

				if (!new EmailValidator().isValid(userDTO.getEmail(), null)) {
					errors.add(new ErrorModel("user.email.invalid.pattern"));
				}
			}
		}

		if (StringUtils.isBlank(userDTO.getPassword())) {
			errors.add(new ErrorModel("user.password.not.blank"));
		} else if (userDTO.getPassword().length() > 500) {
			errors.add(new ErrorModel("user.password.max.length"));
		}

		if (StringUtils.isBlank(userDTO.getRole())) {
			errors.add(new ErrorModel("user.role.not.blank"));
		} else {
			userDTO.setRole(userDTO.getRole().toUpperCase());
			try {
				User.Role.valueOf(userDTO.getRole());
			} catch (IllegalArgumentException e) {
				errors.add(new ErrorModel("user.role.invalid"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationError(errors);
		}
	}

}
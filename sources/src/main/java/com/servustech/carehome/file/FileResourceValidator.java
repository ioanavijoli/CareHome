package com.servustech.carehome.file;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;

/**
 * @author Andrei Groza
 *
 */
@Component
public class FileResourceValidator implements Validator<FileResourceDTO> {

	@Override
	public void validate(
			final FileResourceDTO fileResourceDTO) {
		final List<ErrorModel> errors = new ArrayList<>();

		if (StringUtils.isBlank(fileResourceDTO.getUUID())) {
			errors.add(new ErrorModel("file.uuid.not.null"));
		}

		if (StringUtils.isNotBlank(fileResourceDTO.getName())) {
			if (fileResourceDTO.getName().length() > 100) {
				errors.add(new ErrorModel("file.name.max.length"));
			}
		}

		if (StringUtils.isNotBlank(fileResourceDTO.getDescription())) {
			if (fileResourceDTO.getDescription().length() > 500) {
				errors.add(new ErrorModel("file.description.max.length"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationError(errors);
		}

	}

}

package com.servustech.carehome.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;

@Component
public class UserProfileValidator implements Validator<UserProfileDTO> {

	public final static int AVATAR_SIZE = 5 * 1024 * 1024;

	@Override
	public void validate(UserProfileDTO profile) {
		final List<ErrorModel> errors = new ArrayList<>();

		if (StringUtils.isNotBlank(profile.getGender()) && profile.getGender().length() > 20) {
			errors.add(new ErrorModel("profile.gender.max.length"));
		}
		if (StringUtils.isNotBlank(profile.getReligion()) && profile.getReligion().length() > 100) {
			errors.add(new ErrorModel("profile.religion.max.length"));
		}
		if (profile.getAvatar() != null && profile.getAvatar().length > AVATAR_SIZE) {
			errors.add(new ErrorModel("profile.avatar.max.size"));
		}

		if (profile.getAvatarContentType() != null
				&& !profile.getAvatarContentType().equalsIgnoreCase(MediaType.IMAGE_GIF_VALUE)
				&& !profile.getAvatarContentType().equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE)
				&& !profile.getAvatarContentType().equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE)) {
			errors.add(new ErrorModel("profile.avatar.content.type"));
		}

		if (!errors.isEmpty()) {
			throw new ValidationError(errors);
		}

	}

}

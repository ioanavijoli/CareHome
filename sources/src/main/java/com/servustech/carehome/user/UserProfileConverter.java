package com.servustech.carehome.user;

import com.servustech.carehome.util.ImageUtil;
import org.springframework.stereotype.Component;

import com.servustech.carehome.persistence.model.UserProfile;

@Component
public class UserProfileConverter {

	public UserProfileDTO toDTO(UserProfile userP) {
		UserProfileDTO dto = new UserProfileDTO();
		dto.setUUID(userP.getUUID());
		dto.setFirstname(userP.getFirstname());
		dto.setSurname(userP.getSurname());
		dto.setBirthdate(userP.getBirthdate());
		dto.setGender(userP.getGender());
		dto.setReligion(userP.getReligion());
		dto.setUserUUID(userP.getUserUUID());
//		dto.setAvatar(userP.getAvatar());
		dto.setAvatarContentType(userP.getAvatarContentType());
		dto.setRelationship(userP.getRelationship());
		dto.setSmallAvatar(userP.getSmallAvatar());

		return dto;
	}

	public void toEntity(UserProfileDTO dto, UserProfile entity) {

		entity.setAvatar(dto.getAvatar());
		if (dto.getAvatar() != null) {
			entity.setSmallAvatar(ImageUtil.resizeImage(dto.getAvatar(), ImageUtil.Size.M, dto.getAvatarContentType()));
		}
		entity.setBirthdate(dto.getBirthdate());
		entity.setGender(dto.getGender());
		entity.setReligion(dto.getReligion());
		entity.setUserUUID(dto.getUserUUID());
		entity.setFirstname(dto.getFirstname());
		entity.setSurname(dto.getSurname());
		entity.setAvatarContentType(dto.getAvatarContentType());
		entity.setRelationship(dto.getRelationship());
	}
}

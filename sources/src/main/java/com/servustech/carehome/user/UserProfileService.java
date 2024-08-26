package com.servustech.carehome.user;

import com.servustech.carehome.persistence.model.User;
import com.servustech.carehome.persistence.model.UserProfile;
import com.servustech.mongo.utils.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

	/** Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

	private final UserProfileDAO upDAO;
	private final UserDAO userDAO;
	private final UserProfileConverter upConverter;

	@Autowired
	public UserProfileService(UserProfileDAO userProfileDAO, UserDAO userDAO, UserProfileConverter userProfileConverter) {
		this.upDAO = userProfileDAO;
		this.userDAO = userDAO;
		this.upConverter = userProfileConverter;
	}

	/**
	 * Create or update user profile
	 * 
	 * @param dto
	 * @return
	 */
	public UserProfileDTO saveProfile(UserProfileDTO dto) {

		UserProfile userProfile = new UserProfile();
		User user = this.userDAO.findByUUID(dto.getUserUUID());
		if (user == null) {
			throw new EntityNotFoundException(String.format("User with UUID: %s does not exist", dto.getUUID()));
		}

		if (user.getRole() != User.Role.USER) {
			throw new EntityNotFoundException(String.format("User with UUID: %s is not a USER", dto.getUUID()));
		}

		// if the user profile exists the just update, otherwise create it
		Optional<UserProfile> userProfileOld = this.upDAO.findByUserUUID(dto.getUserUUID());
		if (userProfileOld.isPresent()) {
			userProfile = userProfileOld.get();
			userProfile.prepareForUpdate();
		} else {
			userProfile.prepareForCreate();
		}

		this.upConverter.toEntity(dto, userProfile);

		LOGGER.trace("Saving user profile : {}", userProfile.toString());
		return this.upConverter.toDTO(this.upDAO.saveOrUpdate(userProfile));
	}

	/**
	 * Retrieve user profile
	 * 
	 * @param userUUID
	 * @return
	 */
	public UserProfileDTO retrieveProfile(String userUUID) {
		Optional<UserProfile> profile = this.upDAO.findByUserUUID(userUUID);
		if (profile.isPresent()) {
			return this.upConverter.toDTO(profile.get());
		} else {
			throw new EntityNotFoundException(String.format("Profile for user with UUID: %s does not exist", userUUID));
		}
	}

	public UserProfileDTO retrieveAvatar(String userUUID) {
		Optional<UserProfile> profile = this.upDAO.getAvatar(userUUID);
		LOGGER.trace("Retrieve user's avatar : {}", profile.toString());
		if (profile.isPresent()) {
			return this.upConverter.toDTO(profile.get());
		} else {
			throw new EntityNotFoundException(String.format("Profile for user with UUID: %s does not exist", userUUID));
		}
	}

	/**
	 * Delete user profile
	 * 
	 * @param uuid
	 */
	public void delete(String uuid) {
		this.upDAO.deleteByUserUUID(uuid);

	}

}

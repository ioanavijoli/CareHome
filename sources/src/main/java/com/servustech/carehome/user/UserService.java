/**
 *
 */
package com.servustech.carehome.user;

import com.servustech.carehome.email.MobAllEmailService;
import com.servustech.carehome.persistence.model.User;
import com.servustech.carehome.persistence.model.User.Role;
import com.servustech.carehome.util.exception.BusinessError;
import com.servustech.carehome.util.exception.ErrorMessage;
import com.servustech.carehome.web.security.EncryptedPassword;
import com.servustech.mongo.utils.PaginatedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;

/**
 * User service
 *
 * @author Andrei Groza
 *
 */
@Service
public class UserService {
	/** Logger */
	private static final Logger			LOGGER	= LoggerFactory.getLogger(UserService.class);

	/**
	 * Reference to {@link UserDAO}
	 */
	private final UserDAO				userDAO;

	/**
	 * Reference to {@link UserConverter}
	 */
	private final UserConverter			userConverter;

	/**
	 * Reference to {@link MobAllEmailService}
	 */
	private final MobAllEmailService	mobAllEmailService;

	@Autowired
	public UserService(final UserDAO userDAO, final UserConverter userConverter, final MobAllEmailService mobAllEmailService) {
		this.userDAO = userDAO;
		this.userConverter = userConverter;
		this.mobAllEmailService = mobAllEmailService;
	}

	/**
	 * Create user
	 *
	 * @param userDTO
	 * @return
	 */
	public UserDTO createUser(
			final UserDTO userDTO) {
		if (this.userDAO.findByUsername(userDTO.getUsername()).isPresent()) {
			throw new BusinessError(new ErrorMessage("user.username.already.exists"));
		}
		if (this.userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
			throw new BusinessError(new ErrorMessage("user.email.already.exists"));
		}

		final User user = new User();
		user.prepareForCreate();
		this.userConverter.toEntity(userDTO, user);
		user.setPassword(new EncryptedPassword(userDTO.getPassword()).hash());

		final User createdUser = this.userDAO.save(user);

		this.mobAllEmailService.sendWelcomeMessage(createdUser, userDTO.getPassword());
		return this.userConverter.toDTO(createdUser);
	}

	/**
	 * Retrieve users
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	// @Secured({ "DEVOPS", "ADMIN" })
	// @PreAuthorize("hasRole('ADMIN') OR hasRole('DEVOPS')")
	public PaginatedResult<UserDTO> retrieveUsers(
			final int pageNumber,
			final int pageSize) {
		return new PaginatedResult<>(this.userConverter.toDTOList(this.userDAO.findAll(pageNumber, pageSize).getPayload()), 0, 0, 0);
	}

	/**
	 * Retrieve specific user
	 *
	 * @param userUUID
	 * @return
	 */
	public UserDTO retrieveUserByUUID(
			final String userUUID) {
		return this.userConverter.toDTO(this.userDAO.findByUUID(userUUID));
	}

	/**
	 * Retrieve specific user
	 *
	 * @param email
	 * @return
	 */
	public UserDTO retrieveUserByEmail(
			final String email) {
		return this.userConverter.toDTO(this.userDAO.findByEmail(email).get());
	}

}

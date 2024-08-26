/**
 *
 */
package com.servustech.carehome.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.servustech.carehome.persistence.model.User;
import com.servustech.carehome.persistence.model.User.Role;

/**
 * User converter. Converts {@link User} entity to {@link UserDTO} model
 *
 * @author Andrei Groza
 *
 */
@Component
public class UserConverter {

	public UserDTO toDTO(
			final User user) {
		final UserDTO userDTO = new UserDTO();
		userDTO.setUUID(user.getUUID());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setRole(user.getRole().name());
		return userDTO;
	}

	public List<UserDTO> toDTOList(
			final Collection<User> users) {
		return users.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public void toEntity(
			final UserDTO userDTO,
			final User user) {
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setRole(Role.valueOf(userDTO.getRole()));
	}

}

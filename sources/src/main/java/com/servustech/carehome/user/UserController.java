/**
 *
 */
package com.servustech.carehome.user;

import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.util.DateConverter;
import com.servustech.carehome.util.PaginationValidator;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.ItemResponse;
import com.servustech.carehome.web.model.PaginatedCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User controller
 *
 * @author Andrei Groza
 *
 */
@Api(value = "Users API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/users")
public class UserController {
	/** Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	/**
	 * Reference to {@link UserService}
	 */
	private final UserService userService;

	/**
	 * Reference to {@link UserProfileService}
	 */
	private final UserProfileService userProfileService;

	/**
	 * Reference to {@link UserValidator}
	 */
	private final UserValidator userValidator;

	/**
	 * Reference to {@link UserProfileValidator}
	 */
	private final UserProfileValidator userProfileValidator;
	/**
	 * Reference to {@link PaginationValidator}
	 */
	private final PaginationValidator paginationValidator;

	/**
	 * Constructor
	 *
	 * @param userService
	 *            user service
	 * @param userValidator
	 *            user validator
	 * @param paginationValidator
	 *            pagination validator
	 */
	@Autowired
	public UserController(final UserService userService, final UserProfileService userProfileService,
			final UserValidator userValidator, final UserProfileValidator userProfileValidator,
			final PaginationValidator paginationValidator) {
		this.userService = userService;
		this.userProfileService = userProfileService;
		this.userValidator = userValidator;
		this.userProfileValidator = userProfileValidator;
		this.paginationValidator = paginationValidator;
	}

	/**
	 * Ping users service
	 *
	 * @param request
	 *            the http request
	 * @param response
	 *            the ping response, in a valid html format
	 * @throws IOException
	 *             if error occurs
	 */
	@ApiOperation(value = "Ping users service", produces = MediaType.TEXT_HTML_VALUE, code = 200, response = String.class)
	@RequestMapping(value = "/admin/ping", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public void ping(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		LOGGER.debug("Pinging User Controller from IP {}:{}!", request.getRemoteHost(), request.getRemotePort());

		response.setContentType("text/html");
		response.getOutputStream().print("<h1>User Controller is up and running!</h1>");
	}

	/**
	 * Retrieve all users
	 *
	 * @return a paginated collection of users
	 */
	@ApiOperation(value = "Retrieve all users", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = UserDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedCollectionResponse<UserDTO> getAll(
			@RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int page,
			@RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
		this.paginationValidator.validate(page, pageSize);
		return new PaginatedCollectionResponse<>(this.userService.retrieveUsers(page, pageSize).getPayload(), page,
				pageSize);
	}

	/**
	 * Retrieve user by UUID
	 *
	 * @param userUUID
	 *            the UUID of the user
	 * @return an item containing found user
	 */
	@ApiOperation(value = "Retrieve user by id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = UserDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found", response = RestAPIError.class),
			@ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{user-uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemResponse<UserDTO> getByID(@PathVariable("user-uuid") final String userUUID) {
		LOGGER.debug("Retrieving user with UUID {}", userUUID);

		return new ItemResponse<>(this.userService.retrieveUserByUUID(userUUID));
	}

	/**
	 * Register user
	 *
	 * @param userDTO
	 *            the user model
	 * @return an item containing saved user
	 */
	@ApiOperation(value = "Register new user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201, response = UserDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ItemResponse<UserDTO> registerUser(@RequestBody final UserDTO userDTO) {
		this.userValidator.validate(userDTO);
		return new ItemResponse<>(this.userService.createUser(userDTO));
	}

	/**
	 * Save user profile
	 * @param userUUID
	 * @param birthdate
	 * @param religion
	 * @param gender
	 * @param relationship
	 * @param firstname
	 * @param surename
	 * @param file
     * @return
     * @throws IOException
     */
	@ApiOperation(value = "Save user profile for a specific user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = UserProfileDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{user-uuid}/profile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ItemResponse<UserProfileDTO> saveUserProfile(@PathVariable("user-uuid") final String userUUID,
			@RequestParam(name = "birthdate", required = false, defaultValue = "") final String birthdate,
			@RequestParam(name = "religion", required = false, defaultValue = "") final String religion,
			@RequestParam(name = "gender", required = false, defaultValue = "") final String gender,
			@RequestParam(name = "relationship", required = false, defaultValue = "") final String relationship,
			@RequestParam(name = "firstname", required = false, defaultValue = "") final String firstname,
			@RequestParam(name = "surname", required = false, defaultValue = "") final String surename,
			@RequestParam(name = "avatar", required = false) final MultipartFile file) throws IOException {

		final UserProfileDTO dto = new UserProfileDTO();
		dto.setUserUUID(userUUID);
		if (!birthdate.isEmpty()) {
			dto.setBirthdate(DateConverter.toLocalDate(birthdate));
		}
		if (file != null) {
			dto.setAvatar(StreamUtils.copyToByteArray(file.getInputStream()));
			dto.setAvatarContentType(file.getContentType());
		}
		dto.setReligion(religion);
		dto.setGender(gender);
		dto.setRelationship(relationship);
		dto.setFirstname(firstname);
		dto.setSurname(surename);

		this.userProfileValidator.validate(dto);

		return new ItemResponse<>(this.userProfileService.saveProfile(dto));
	}

	/**
	 * Retrieve user profile
	 * 
	 * @param userUUID
	 * @return
	 */

	@ApiOperation(value = "Retrieve user profile by user uuid", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = UserProfileDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found", response = RestAPIError.class),
			@ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{user-uuid}/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemResponse<UserProfileDTO> getProfile(@PathVariable("user-uuid") final String userUUID) {
		LOGGER.debug("Retrieving user profile with UUID {}", userUUID);

		return new ItemResponse<>(this.userProfileService.retrieveProfile(userUUID));
	}

	/**
	 * Delete user profile
	 * 
	 * @param uuid
	 */
	@ApiOperation(value = "Remove user profile", code = 204)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "User profile not found", response = RestAPIError.class),
			@ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{user-uuid}/profile", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ResponseBody
	public void deleteFile(@PathVariable("user-uuid") final String uuid) {

		this.userProfileService.delete(uuid);
	}

	@ApiOperation(value = "Retrieve file resource", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, code = 200, response = byte[].class, notes = "Content type may have different values")
	@RequestMapping(value = "/{user-uuid}/profile/avatar", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getAvatar(@PathVariable("user-uuid") final String userUUID) {

		UserProfileDTO profile = this.userProfileService.retrieveAvatar(userUUID);

		final HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.valueOf(profile.getAvatarContentType()));

		return new ResponseEntity<>(profile.getSmallAvatar(), responseHeaders, HttpStatus.OK);
	}
}

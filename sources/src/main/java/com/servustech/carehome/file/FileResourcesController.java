/**
 *
 */
package com.servustech.carehome.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

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

import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.util.DateConverter;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.CollectionResponse;
import com.servustech.carehome.web.model.ItemResponse;

/**
 * @author Andrei Groza
 *
 */
@Api(value = "FileResource API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/files")
public class FileResourcesController {
	private static final Logger		LOGGER	= LoggerFactory.getLogger(FileResourcesController.class);

	/** Reference to {@link FileResourceService} */
	@Autowired
	private FileResourceService		fileResourceService;

	/** Reference to {@link FileResourceValidator} */
	@Autowired
	private FileResourceValidator	fileResourceValidator;

	/**
	 * Ping file resources service
	 *
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation(value = "Ping file resources service", produces = MediaType.TEXT_HTML_VALUE, code = 200, response = String.class)
	@RequestMapping(value = "/file-resources/admin/ping", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public void ping(
			final HttpServletResponse response) throws IOException {
		LOGGER.trace("FileResourcesController service is up and running!");
		response.setContentType("text/html");
		response.getOutputStream().print("<h1>FileResourcesController service is up and running!</h1>");
	}

	/**
	 * Save a file resources
	 *
	 * @param name
	 *            the file name
	 * @param description
	 *            the file description
	 * @param contentType
	 *            content type of the file
	 * @param file
	 *            file as a byte array
	 * @return the saved file
	 * @throws IOException
	 */
	@ApiOperation(value = "Save file resource", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/file-resources", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public ItemResponse<FileResourceDTO> saveFile(
			@RequestParam(name = "name", required = false) final String name,
			@RequestParam(name = "description", required = false) final String description,
			@RequestParam(name = "contentType", required = true) final String contentType,
			@RequestParam("file") final MultipartFile file) throws IOException {
		LOGGER.debug("Uploading file resource with name {}, having {} bytes size", name, file.getSize());

		final FileResourceDTO fileResourceDTO = new FileResourceDTO();
		fileResourceDTO.setName(name);
		fileResourceDTO.setDescription(description);
		fileResourceDTO.setContentType(contentType);
		fileResourceDTO.setContent(StreamUtils.copyToByteArray(file.getInputStream()));
		fileResourceDTO.setOriginalName(file.getOriginalFilename());
		return new ItemResponse<>(this.fileResourceService.saveFileResource(fileResourceDTO));
	}

	/**
	 * Save file for a specific user
	 *
	 * @param userUUID
	 *            the uuid of the user
	 * @param name
	 *            the file name
	 * @param description
	 *            the file description
	 * @param contentType
	 *            the content type of the file
	 * @param file
	 *            the file as a byte array
	 * @return the file model
	 * @throws IOException
	 */
	@ApiOperation(value = "Save file resource for a specific user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/users/{user-uuid}/file-resources", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public ItemResponse<FileResourceDTO> saveUserFile(
			@PathVariable("user-uuid") final String userUUID,
			@RequestParam(name = "name", required = false) final String name,
			@RequestParam(name = "description", required = false) final String description,
			@RequestParam(name = "contentType", required = true) final String contentType,
			@RequestParam("file") final MultipartFile file) throws IOException {
		LOGGER.debug("Uploading file resource with name {}, having {} bytes size", name, file.getSize());

		final FileResourceDTO fileResourceDTO = new FileResourceDTO();
		fileResourceDTO.setUserUUID(userUUID);
		fileResourceDTO.setName(name);
		fileResourceDTO.setDescription(description);
		fileResourceDTO.setContentType(contentType);
		fileResourceDTO.setContent(StreamUtils.copyToByteArray(file.getInputStream()));
		fileResourceDTO.setOriginalName(file.getOriginalFilename());
		return new ItemResponse<>(this.fileResourceService.saveFileResource(fileResourceDTO));
	}

	/**
	 * Retrieve file resource
	 *
	 * @param uuid
	 *            the uuid of the file resource
	 * @return the file as a byte array
	 */
	@ApiOperation(value = "Retrieve file resource", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, code = 200, response = byte[].class, notes = "Content type may have different values")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class),
			@ApiResponse(code = 404, message = "File resource not found", response = RestAPIError.class) })
	@RequestMapping(value = "/file-resources/{file-uuid}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFile(
			@PathVariable("file-uuid") final String uuid) {
		final FileResourceDTO fileResourceDTO = this.fileResourceService.getFileResourceByUUID(uuid);

		final HttpHeaders responseHeaders = new HttpHeaders();
		if (fileResourceDTO.getContentType() == null) {
			responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		} else {
			try {
				responseHeaders.setContentType(MediaType.valueOf(fileResourceDTO.getContentType()));
			} catch (final Exception e) {
				LOGGER.error("Invalid media type: " + fileResourceDTO.getContentType(), e);
				responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			}
		}
		return new ResponseEntity<>(fileResourceDTO.getContent(), responseHeaders, HttpStatus.OK);
	}

	/**
	 * Retrieve file resources for a specific user
	 *
	 * @param userUUID
	 *            the uuid of the user
	 * @return the collection of all file resources saved by a user
	 */
	@ApiOperation(value = "Retrieve file resources for a specific user", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/users/{user-uuid}/file-resources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public CollectionResponse<FileResourceDTO> retrieveUserFiles(
			@PathVariable("user-uuid") final String userUUID,
			@RequestParam(name = "name", required = false, defaultValue = "") final String name,
			@RequestParam(name = "keyword", required = false, defaultValue = "") final String keyword,
			@RequestParam(name = "startDate", required = false, defaultValue = "") final String startDate,
			@RequestParam(name = "endDate", required = false, defaultValue = "") final String endDate) {

		final Optional<String> optionalName = name.isEmpty() ? Optional.empty() : Optional.of(name);
		final Optional<String> optionalKeyword = keyword.isEmpty() ? Optional.empty() : Optional.of(keyword);
		final Optional<LocalDateTime> optionalStartDate = startDate.isEmpty() ? Optional.empty() : Optional.of(DateConverter.toLocalDateTime(startDate
				+ " 00:00:00"));
		final Optional<LocalDateTime> optionalEndDate = endDate.isEmpty() ? Optional.empty() : Optional
				.of(DateConverter.toLocalDateTime(endDate + " 00:00:00"));
		return new CollectionResponse<>(this.fileResourceService.getFileResources(userUUID, optionalName, optionalKeyword, optionalStartDate, optionalEndDate));
	}

	/**
	 * Update existing file resource
	 *
	 * @param uuid
	 *            the uuid of the file resource
	 * @param fileResourceDTO
	 *            the file resource model
	 * @return the updated file resource
	 */
	@ApiOperation(value = "Update existing file resource", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "File resource not found", response = RestAPIError.class),
			@ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/file-resources/{file-uuid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ItemResponse<FileResourceDTO> updateFileResource(
			@PathVariable("file-uuid") final String uuid,
			@RequestBody final FileResourceDTO fileResourceDTO) {
		fileResourceDTO.setUUID(uuid);
		this.fileResourceValidator.validate(fileResourceDTO);

		return new ItemResponse<>(this.fileResourceService.updateFileResource(fileResourceDTO));
	}

	/**
	 * Delete file resource
	 *
	 * @param uuid
	 *            the uuid of the file resource
	 */
	@ApiOperation(value = "Remove file user", code = 204)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "File resource not found", response = RestAPIError.class),
			@ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/file-resources/{file-uuid}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ResponseBody
	public void deleteFile(
			@PathVariable("file-uuid") final String uuid) {

		this.fileResourceService.deleteFileResource(uuid);
	}
}

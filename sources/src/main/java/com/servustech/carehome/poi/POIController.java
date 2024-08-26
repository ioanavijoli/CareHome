package com.servustech.carehome.poi;

import com.servustech.carehome.comment.CommentService;
import com.servustech.carehome.comment.CommentsDTO;
import com.servustech.carehome.file.FileResourceDTO;
import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.util.PaginationValidator;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.CollectionResponse;
import com.servustech.carehome.web.model.ItemResponse;
import com.servustech.carehome.web.model.PaginatedCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@Api(value = "Point of interests API", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/carehome")
public class POIController {

	private final POIService poiService;

	private final PaginationValidator pagValidator;

	private final CommentService commentService;

	@Autowired
	public POIController(POIService poiService, PaginationValidator pagValidator, CommentService commentService) {
		this.poiService = poiService;
		this.pagValidator = pagValidator;
		this.commentService = commentService;
	}

	/**
	 * Create new point of interest
	 * 
	 * @param dto
	 * @return
	 */
	/*@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ItemResponse<PointOfInterestDTO> createPOI(@RequestBody final PointOfInterestDTO dto) {

		return new ItemResponse<>(this.poiService.createPOI(dto));
	}*/

	/**
	 * Update point of interest
	 * 
	 * @param poiUUID
	 * @param dto
	 * @return
	 */
	/*@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{poi-uuid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ItemResponse<PointOfInterestDTO> updatePOI(@PathVariable("poi-uuid") final String poiUUID,
			@RequestBody final PointOfInterestDTO dto) {

		return new ItemResponse<>(this.poiService.updatePOI(poiUUID, dto));
	}*/

	/**
	 * Retrieve point of interest
	 * 
	 * @param poiUUID
	 * @return
	 */
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{care-uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ItemResponse<PointOfInterestDTO> getPOI(@PathVariable("care-uuid") final String poiUUID) {

		return new ItemResponse<>(this.poiService.getPOI(poiUUID));
	}

	/**
	 * Delete point of interest
	 * 
	 * @param poiUUID
	 */
	/*@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{poi-uuid}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deletePOI(@PathVariable("poi-uuid") final String poiUUID) {

		this.poiService.deletePOI(poiUUID);
	}*/

	/**
	 * Save picture for a given point of interest
	 * 
	 * @param poiUUID
	 * @param name
	 * @param description
	 * @param contentType
	 * @param file
	 * @return
	 * @throws IOException
	 */
	/*@ApiOperation(value = "Save picture for poi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{poi-uuid}/pictures", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public ItemResponse<FileResourceDTO> saveFile(@PathVariable("poi-uuid") final String poiUUID,
			@RequestParam(name = "name", required = false) final String name,
			@RequestParam(name = "description", required = false) final String description,
			@RequestParam(name = "contentType", required = true) final String contentType,
			@RequestParam("picture") final MultipartFile file) throws IOException {

		final FileResourceDTO fileResourceDTO = new FileResourceDTO();
		fileResourceDTO.setName(name);
		fileResourceDTO.setDescription(description);
		fileResourceDTO.setContentType(contentType);
		fileResourceDTO.setContent(StreamUtils.copyToByteArray(file.getInputStream()));
		fileResourceDTO.setOriginalName(file.getOriginalFilename());
		return new ItemResponse<>(this.poiService.savePicture(poiUUID, fileResourceDTO));
	}*/

	/**
	 * Return a list of UUIDs
	 * 
	 * @param poiUUID
	 * @return
	 */
	@ApiOperation(value = "Retrieve pictures for a given poi", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{poi-uuid}/pictures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public CollectionResponse<FileResourceDTO> retrievePOIFiles(@PathVariable("poi-uuid") final String poiUUID) {

		return new CollectionResponse<>(this.poiService.getPictures(poiUUID));
	}

	/*
	@ApiOperation(value = "Retrieve picturesUUIDS for a given poi", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{poi-uuid}/picturesUUIDs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public CollectionResponse<String> retrievePOIFilesUUIDs(@PathVariable("poi-uuid") final String poiUUID) {

		return new CollectionResponse<>(this.poiService.getPicturesUUIDs(poiUUID));
	}*/

	/**
	 * Delete picture for a given point of interest
	 * 
	 * @param poiUUID
	 * @param picUUID
	 */
	/*@ApiOperation(value = "Delete pictures for a given poi", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{poi-uuid}/pictures/{pic-uuid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deletePicture(@PathVariable("poi-uuid") final String poiUUID,
			@PathVariable("pic-uuid") final String picUUID) {
		poiService.deletePicture(poiUUID, picUUID);
	}*/

	/**
	 * Retrieve points of interest in a paginated manner
	 *
	 * @return
	 */
	@ApiOperation(value = "Retrieve all poi", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedCollectionResponse<PointOfInterestDTO> getAll(
			@RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
			@RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
		this.pagValidator.validate(pageNumber, pageSize);
		return new PaginatedCollectionResponse<>(this.poiService.retrieveAll(pageNumber, pageSize), pageNumber, pageSize);
	}

	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/{care-uuid}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ItemResponse<CommentsDTO> getCareHomeComments(
			@PathVariable("care-uuid") final String carehomeUUID) {

		return new ItemResponse<>(commentService.getComments(carehomeUUID));

	}

	/** Retrieve points of interest by a list of UUIDs, keeping the request order
	 *
	 * @param uuids
	 * @return
	 */
	@ApiOperation(value = "Retrieve points of interest by price interval", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/compare", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public PaginatedCollectionResponse<PointOfInterestDTO> retrieveByUUIDs(
			@RequestBody final PointOfInterestUUID uuids) {
		Collection<PointOfInterestDTO> pointOfInterestDTOs = poiService.retrieveByUUIDList(uuids);
		return new PaginatedCollectionResponse<>(pointOfInterestDTOs, 1, pointOfInterestDTOs.size());
	}

	/** Retrieve points of interest by a list of UUIDs, keeping the request order
	 *
	 * @param uuids
	 * @return
	 */
	@ApiOperation(value = "Retrieve points of interest by price interval", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
	@RequestMapping(value = "/compare/rating", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public PaginatedCollectionResponse<PointOfInterestDTO> compareCarehomesByRating(
			@RequestBody final PointOfInterestUUID uuids) {
		Collection<PointOfInterestDTO> pointOfInterestDTOs = poiService.compareByPrice(uuids);
		return new PaginatedCollectionResponse<>(pointOfInterestDTOs, 1, pointOfInterestDTOs.size());
	}
}

package com.servustech.carehome.business;

import com.servustech.carehome.file.FileResourceDTO;
import com.servustech.carehome.poi.PointOfInterestDTO;
import com.servustech.carehome.user.UserController;
import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.CollectionResponse;
import com.servustech.carehome.web.model.ItemResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "Business API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/users/{user-uuid}/business")
public class BusinessController {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final BusinessValidator businessValidator;

    private final BusinessService businessService;

    @Autowired
    public BusinessController(final BusinessValidator businessValidator, final BusinessService businessService) {
        this.businessValidator = businessValidator;
        this.businessService = businessService;
    }

    @ApiOperation(value = "Ping users service", produces = MediaType.TEXT_HTML_VALUE, code = 200, response = String.class)
    @RequestMapping(value = "/admin/ping", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public void ping(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        LOGGER.debug("Pinging User Controller from IP {}:{}!", request.getRemoteHost(), request.getRemotePort());

        response.setContentType("text/html");
        response.getOutputStream().print("<h1>Business Controller is up and running!</h1>");
    }

    /**
     * Get business for user
     * @param userUUID
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ItemResponse<BusinessDTO> getBusiness(@PathVariable("user-uuid") final String userUUID){
        return new ItemResponse<>(this.businessService.getBusiness(userUUID));
    }

    /**
     * Create business for user
     * @param userUUID
     * @param dto
     * @return
     */
    @ApiOperation(value = "Add business to a user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201, response = BusinessDTO.class, notes = "Response is wrapped as 'item'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ItemResponse<BusinessDTO> addBusiness(@PathVariable("user-uuid") final String userUUID,
                                                 @RequestBody final BusinessDTO dto){

        this.businessValidator.validate(dto);
        dto.setUserUUID(userUUID);
        return new ItemResponse<>(this.businessService.createBusiness(dto));

    }

    /**
     * Update business for user
     * @param userUUID
     * @param dto
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ItemResponse<BusinessDTO> updateBusiness(@PathVariable("user-uuid") final String userUUID,
                                                    @RequestBody final BusinessDTO dto) {

        this.businessValidator.validate(dto);
        dto.setUserUUID(userUUID);
        return new ItemResponse<>(this.businessService.updateBusiness(dto));
    }


    @ApiOperation(value = "Save logo for business", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/logo", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public void saveLogo(@PathVariable("user-uuid") final String userUUID,
                         @RequestParam(name = "picture") final MultipartFile file, HttpServletResponse response) throws IOException {
        response.getOutputStream().write(businessService.saveLogo(userUUID, file.getContentType(), StreamUtils.copyToByteArray(file.getInputStream())));
    }


    /**
     * Save picture for a business
     * @param userUUID
     * @param name
     * @param description
     * @param contentType
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "Save picture for poi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/pictures", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ItemResponse<FileResourceDTO> saveFile(@PathVariable("user-uuid") final String userUUID,
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
        return new ItemResponse<>(this.businessService.savePicture(userUUID, fileResourceDTO));
    }

    /**
     * Remove picture of business
     * @param userUUID
     * @param picUUID
     */
    @ApiOperation(value = "Delete pictures for business", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/pictures/{pic-uuid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePicture(@PathVariable("user-uuid") final String userUUID,
                              @PathVariable("pic-uuid") final String picUUID) {
        businessService.deletePicture(userUUID, picUUID);
    }

    /**
     * Add care home to a business
     * @param userUUID
     * @param dto
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ItemResponse<PointOfInterestDTO> createPOI(
            @PathVariable("user-uuid") final String userUUID,
            @RequestBody final PointOfInterestDTO dto) {

        return new ItemResponse<>(this.businessService.createCareHome(userUUID, dto));
    }

    /**
     * Retrieve all care homes of a business
     * @param userUUID
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CollectionResponse<PointOfInterestDTO> getCareHomes(
            @PathVariable("user-uuid") final String userUUID) {

        return new CollectionResponse<>(this.businessService.getCareHomes(userUUID));
    }

    /**
     * Update carehome for business
     * @param userUUID
     * @param poiUUID
     * @param dto
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes/{poi-uuid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ItemResponse<PointOfInterestDTO> updatePOI(@PathVariable("user-uuid") final String userUUID,
            @PathVariable("poi-uuid") final String poiUUID, @RequestBody final PointOfInterestDTO dto) {

        return new ItemResponse<>(this.businessService.updateCareHome(userUUID, poiUUID, dto));
    }

    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes/{poi-uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ItemResponse<PointOfInterestDTO> getPOI(@PathVariable("user-uuid") final String userUUID,
                                                   @PathVariable("poi-uuid") final String poiUUID) {

        return new ItemResponse<>(this.businessService.getCarehome(userUUID, poiUUID));
    }

    /**
     * Delete carehome
     *
     * @param poiUUID
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes/{poi-uuid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePOI(@PathVariable("user-uuid") final String userUUID,
                          @PathVariable("poi-uuid") final String poiUUID) {

        this.businessService.deleteCareHome(userUUID, poiUUID);
    }

    /**
     * save pciture for carehome
     * @param userUUID
     * @param poiUUID
     * @param name
     * @param description
     * @param contentType
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "Save picture for poi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, code = 201, response = FileResourceDTO.class, notes = "Response is wrapped as 'item'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes/{poi-uuid}/pictures", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ItemResponse<FileResourceDTO> saveFile(@PathVariable("user-uuid") final String userUUID,
                                                  @PathVariable("poi-uuid") final String poiUUID,
                                                  @RequestParam(name = "name", required = false) final String name,
                                                  @RequestParam(name = "description", required = false) final String description,
                                                  @RequestParam(name = "contentType", required = true) final String contentType,
                                                  @RequestParam("picture") final MultipartFile file,
                                                  @RequestParam(name = "firstPicture", required = false) final boolean firstPicture) throws IOException {

        final FileResourceDTO fileResourceDTO = new FileResourceDTO();
        fileResourceDTO.setName(name);
        fileResourceDTO.setDescription(description);
        fileResourceDTO.setContentType(contentType);
        fileResourceDTO.setContent(StreamUtils.copyToByteArray(file.getInputStream()));
        fileResourceDTO.setOriginalName(file.getOriginalFilename());
        return new ItemResponse<>(this.businessService.saveCareHomePicture(userUUID, poiUUID, fileResourceDTO, firstPicture));
    }

    /**
     * Retrieve all pictures of a carehome
     * @param poiUUID
     * @return
     */
    @ApiOperation(value = "Retrieve pictures for a given poi", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes/{poi-uuid}/pictures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CollectionResponse<FileResourceDTO> retrievePOIFiles(@PathVariable("user-uuid") final String userUUID,
                                                                @PathVariable("poi-uuid") final String poiUUID) {

        return new CollectionResponse<>(this.businessService.getCareHomePictures(userUUID, poiUUID));
    }

    /**
     * Delete picture from a carehome
     * @param userUUID
     * @param poiUUID
     * @param picUUID
     */
    @ApiOperation(value = "Delete pictures for a given poi", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = FileResourceDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/carehomes/{poi-uuid}/pictures/{pic-uuid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePicture(@PathVariable("user-uuid") final String userUUID,
                              @PathVariable("poi-uuid") final String poiUUID,
                              @PathVariable("pic-uuid") final String picUUID) {
        businessService.deleteCareHomePicture(userUUID, poiUUID, picUUID);
    }
}

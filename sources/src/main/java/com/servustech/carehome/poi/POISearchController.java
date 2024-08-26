package com.servustech.carehome.poi;

import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.util.PaginationValidator;
import com.servustech.carehome.util.RatingValidator;
import com.servustech.carehome.util.SearchByAddressValidator;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.PaginatedCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by IDinu on 12/15/2016.
 */
@Api(value = "Point of interests API", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/poi/search/by")
public class POISearchController {

    private final POIService poiService;

    private final PaginationValidator pagValidator;

    private final SearchByAddressValidator searchAddrValidator;

    private final RatingValidator ratingValidator;

    @Autowired
    public POISearchController(POIService poiService,
                               PaginationValidator pagValidator,
                               SearchByAddressValidator searchAddrValidator,
                               RatingValidator ratingValidator) {
        this.poiService = poiService;
        this.pagValidator = pagValidator;
        this.searchAddrValidator = searchAddrValidator;
        this.ratingValidator = ratingValidator;
    }


    /** Retrieve points of interest by name
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "Retrieve points of interest by name", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginatedCollectionResponse<PointOfInterestDTO> retrieveByName(
            @RequestParam(name = "name", required = true) final String careHomeName,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
        this.pagValidator.validate(pageNumber, pageSize);
        Collection<PointOfInterestDTO> paginatedResult = this.poiService.retrieveByName(careHomeName,
                                                                                        pageNumber,
                                                                                        pageSize);
        return new PaginatedCollectionResponse<>(paginatedResult, pageNumber, pageSize);
    }

    /** Retrieve points of interest by zipcode
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "Retrieve points of interest by zipcode", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/address", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginatedCollectionResponse<PointOfInterestDTO> retrieveByZipCode(
            @RequestParam(name = "zipCode", required = false) final Integer zipCode,
            @RequestParam(name = "zipCodeRange", required = false) final Integer zipCodeRange,
            @RequestParam(name = "country", required = false) final String country,
            @RequestParam(name = "city", required = false) final String city,
            @RequestParam(name = "street", required = false) final String street,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
        this.pagValidator.validate(pageNumber, pageSize);
        this.searchAddrValidator.validate(zipCode, zipCodeRange, country, city, street);
        Collection<PointOfInterestDTO> paginatedResult = this.poiService.retrieveByAddress(zipCode, zipCodeRange,
                                                                                           country, city, street,
                                                                                           pageNumber, pageSize);
        return new PaginatedCollectionResponse<>(paginatedResult, pageNumber, pageSize);
    }


    /** Retrieve points of interest by zipcode
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "Retrieve points of interest by zipcode", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/region", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginatedCollectionResponse<PointOfInterestDTO> retrieveByRegion(
            @RequestParam(name = "postalCode", required = true) String postalCode,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
        this.pagValidator.validate(pageNumber, pageSize);
        this.searchAddrValidator.validate(postalCode);
        Collection<PointOfInterestDTO> paginatedResult = this.poiService.retrieveByRegion(postalCode,
                                                                                          pageNumber, pageSize);
        return new PaginatedCollectionResponse<>(paginatedResult, pageNumber, pageSize);
    }

    /** Retrieve points of interest by rating interval
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "Retrieve points of interest by rating interval", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/rating", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginatedCollectionResponse<PointOfInterestDTO> retrieveByRating(
            @RequestParam(name = "min", required = true) final Double minValue,
            @RequestParam(name = "max", required = true) final Double maxValue,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
        this.pagValidator.validate(pageNumber, pageSize);
        this.ratingValidator.validate(minValue, maxValue);
        Collection<PointOfInterestDTO> paginatedResult = this.poiService.retrieveByRatingInterval(minValue, maxValue,
                                                                                                  pageNumber, pageSize);
        return new PaginatedCollectionResponse<>(paginatedResult, pageNumber, pageSize);
    }

    /** Retrieve points of interest by price interval
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "Retrieve points of interest by price interval", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = PointOfInterestDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/price", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginatedCollectionResponse<PointOfInterestDTO> retrieveByPrice(
            @RequestParam(name = "min", required = true) final Double minValue,
            @RequestParam(name = "max", required = true) final Double maxValue,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
        this.pagValidator.validate(pageNumber, pageSize);
        this.ratingValidator.validate(minValue, maxValue);
        Collection<PointOfInterestDTO> paginatedResult = this.poiService.retrieveByPriceInterval(minValue, maxValue,
                                                                                                 pageNumber, pageSize);
        return new PaginatedCollectionResponse<>(paginatedResult, pageNumber, pageSize);
    }

}

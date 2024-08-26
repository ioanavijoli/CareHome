package com.servustech.carehome.business;

import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.util.PaginationValidator;
import com.servustech.carehome.web.RestAPIError;
import com.servustech.carehome.web.model.PaginatedCollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Api(value = "Business public API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/business")
public class BusinessPublicController {

    private final BusinessService businessService;

    private final PaginationValidator pagValidator;

    @Autowired
    public BusinessPublicController(BusinessService businessService, PaginationValidator pagValidator){
        this.businessService = businessService;
        this.pagValidator = pagValidator;
    }

    /** Retrieve businesses
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "Retrieve businesses", produces = MediaType.APPLICATION_JSON_VALUE, code = 200, response = BusinessDTO.class, notes = "Response is wrapped as 'items'")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginatedCollectionResponse<BusinessDTO> getAll(
            @RequestParam(name = AppConstants.PAGINATION_PAGE_NUMBER, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUMBER) final int pageNumber,
            @RequestParam(name = AppConstants.PAGINATION_PAGE_SIZE, required = false, defaultValue = AppConstants.PAGINATION_DEFAULT_SIZE) final int pageSize) {
        this.pagValidator.validate(pageNumber, pageSize);
        return new PaginatedCollectionResponse<>(this.businessService.retrieveAll(pageNumber, pageSize).getPayload(),
                pageNumber, pageSize);
    }
}

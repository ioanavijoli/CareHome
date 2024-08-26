package com.servustech.carehome.comment;

import com.servustech.carehome.poi.PointOfInterestDTO;
import com.servustech.carehome.util.AppConstants;
import com.servustech.carehome.web.RestAPIError;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "Comments API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/users/{user-uuid}/comments")
public class CommentController {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @Autowired
    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Ping users service", produces = MediaType.TEXT_HTML_VALUE, code = 200, response = String.class)
    @RequestMapping(value = "/admin/ping", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public void ping(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        LOGGER.debug("Pinging Comment Controller from IP {}:{}!", request.getRemoteHost(), request.getRemotePort());

        response.setContentType("text/html");
        response.getOutputStream().print("<h1>Comment Controller is up and running!</h1>");
    }

    /**
     * Create a comment
     * @param userUUID
     * @param dto
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ItemResponse<CommentDTO> createComment(@PathVariable("user-uuid") final String userUUID,
                                                          @RequestBody final CommentDTO dto) {

        dto.setUserUUID(userUUID);
        return new ItemResponse<>(this.commentService.createComment(dto));
    }

    /**
     * Update comment
     * @param userUUID
     * @param commentUUID
     * @param dto
     * @return
     */
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal server error", response = RestAPIError.class) })
    @RequestMapping(value = "/{comment-uuid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ItemResponse<CommentDTO> updatePOI(@PathVariable("user-uuid") final String userUUID,
                                                      @PathVariable("comment-uuid") final String commentUUID,
                                                      @RequestBody final CommentDTO dto) {

        dto.setUserUUID(userUUID);
        return new ItemResponse<>(this.commentService.updateComment(commentUUID, dto));
    }
}

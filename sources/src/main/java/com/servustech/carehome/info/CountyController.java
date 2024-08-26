package com.servustech.carehome.info;


import com.servustech.carehome.util.AppConstants;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "County API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/counties")
@CrossOrigin
public class CountyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountyController.class);

    @Autowired
    private CountyService countyService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<CountyDTO>> getAllCounties() {
        List<CountyDTO> countyList = countyService.getAllCounties();
        return new ResponseEntity<>(countyList, HttpStatus.OK);
    }
}
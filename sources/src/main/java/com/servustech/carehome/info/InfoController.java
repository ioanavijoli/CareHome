package com.servustech.carehome.info;

import com.servustech.carehome.util.AppConstants;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Info API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = AppConstants.API_V1_PREFIX + "/info")
@CrossOrigin

public class InfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private InfoService infoService;

    @RequestMapping(value = "/searchByCity", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByCity(
            @RequestParam("city") String city,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoPage = infoService.searchByCity(city, page, pageSize);
        return getMapResponseEntity(infoPage);
    }

    @RequestMapping(value = "/searchByCountry", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByCountry(
            @RequestParam("country") String country,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoPage = infoService.searchByCountry(country, page, pageSize);
        return getMapResponseEntity(infoPage);
    }

    @RequestMapping(value = "/searchByPostalCode", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByPostalCode(
            @RequestParam("postalCode") String postalCode,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoPage = infoService.searchByPostalCode(postalCode, page, pageSize);
        return getMapResponseEntity(infoPage);
    }

    @RequestMapping(value = "/searchByStreet", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByStreet(
            @RequestParam("street") String street,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoPage = infoService.searchByStreet(street, page, pageSize);
        return getMapResponseEntity(infoPage);
    }


    @RequestMapping(value = "/searchByLocation", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByLocation(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoPage = infoService.searchByLocation(query, page, pageSize);
        return getMapResponseEntity(infoPage);
    }

    @RequestMapping(value = "/searchByName", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByName(
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoList = infoService.searchByName(name, page, pageSize);
        return getMapResponseEntity(infoList);
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<InfoDTO> infoPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("page", infoPage.getNumber());
        response.put("pageSize", infoPage.getSize());
        response.put("itemCount", infoPage.getTotalElements());
        response.put("items", infoPage.getContent());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllInfo() {
        List<InfoDTO> infoList = infoService.getAll();
        Map<String, Object> response = new HashMap<>();
        response.put("page", 1);
        response.put("pageSize", 30);
        response.put("itemCount", infoList.size());
        response.put("items", infoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('BUSINESS')")
    public ResponseEntity<?> createInfo(@RequestBody InfoDTO infoDTO) {
        if (infoDTO == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Carehome cannot be null.");
        }

        if (infoDTO.getName() == null || infoDTO.getName().isEmpty()) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        }

        if (infoDTO.getEmail() == null || infoDTO.getEmail().isEmpty()) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Email cannot be empty.");
        }

        if (infoService.existsByName(infoDTO.getName())) {
            return createErrorResponse(HttpStatus.CONFLICT, "An info entry with this name already exists.");
        }
        InfoDTO createdInfo = infoService.create(infoDTO);
        return new ResponseEntity<>(createdInfo, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('BUSINESS')")
    public ResponseEntity<?> updateInfo(@PathVariable("id") String id, @RequestBody InfoDTO infoDTO) {
        InfoDTO existingInfo = infoService.getById(id);
        if (existingInfo == null) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "No carehome with given ID found.");
        }
        InfoDTO updatedInfo = infoService.update(id, infoDTO);
        return new ResponseEntity<>(updatedInfo, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('BUSINESS')")
    public ResponseEntity<?> deleteInfo(@PathVariable("id") String id) {
        InfoDTO infoDTO = infoService.getById(id);
        if (infoDTO == null) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "No carehome with given ID found.");
        }
        infoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoById(@PathVariable("id") String id) {
        LOGGER.debug("Retrieving info entity with ID: {}", id);
        InfoDTO infoDTO = infoService.getById(id);
        if (infoDTO == null) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "No carehome with given ID found.");
        }
        return new ResponseEntity<>(infoDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchByLocationAndType", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByLocationAndType(
            @RequestParam("location") String location,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        Page<InfoDTO> infoPage = infoService.searchByLocationAndType(location, type, page, pageSize);
        return getMapResponseEntity(infoPage);
    }
    @RequestMapping(value = "/searchByService", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchByService(
            @RequestParam("service") String service,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {

        Page<InfoDTO> infoPage = infoService.searchByService(service, page, pageSize);
        return getMapResponseEntity(infoPage);
    }

    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public ResponseEntity<List<InfoDTO>> getAllByIds(@RequestParam List<String> ids) {
        return new ResponseEntity<>(infoService.findAllByIds(ids), HttpStatus.OK);
    }
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String errorMsg) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", status.value());
        response.put("errorKey", status.getReasonPhrase().toUpperCase());
        response.put("errorMsg", errorMsg);
        return ResponseEntity.status(status).body(response);
    }
}

package com.servustech.carehome.business;

import com.servustech.carehome.comment.CommentService;
import com.servustech.carehome.file.FileResourceDTO;
import com.servustech.carehome.file.FileResourceService;
import com.servustech.carehome.google.maps.GeoCodingService;
import com.servustech.carehome.persistence.model.Business;
import com.servustech.carehome.persistence.model.User;
import com.servustech.carehome.poi.POIService;
import com.servustech.carehome.poi.PointOfInterestDTO;
import com.servustech.carehome.user.UserDAO;
import com.servustech.carehome.util.ImageUtil;
import com.servustech.carehome.util.exception.BusinessError;
import com.servustech.carehome.util.exception.ErrorMessage;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import com.servustech.mongo.utils.PaginatedResult;
import com.servustech.mongo.utils.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    private final BusinessDAO businessDAO;

    private final BusinessConverter businessConverter;

    private final UserDAO userDAO;

    private final FileResourceService fileResourceService;

    private final POIService careHomeService;

    private final CommentService commentService;

    private final GeoCodingService geoCodingService;

    @Autowired
    public BusinessService(BusinessDAO businessDAO, UserDAO userDAO, BusinessConverter businessConverter, FileResourceService fileResourceService, POIService careHomeService, CommentService commentService,
                           GeoCodingService geoCodingService) {
        this.businessDAO = businessDAO;
        this.userDAO = userDAO;
        this.businessConverter = businessConverter;
        this.fileResourceService = fileResourceService;
        this.careHomeService = careHomeService;
        this.commentService = commentService;
        this.geoCodingService = geoCodingService;
    }

    /**
     * Create new business
     * @param dto
     * @return
     */
    public BusinessDTO createBusiness(BusinessDTO dto) {

        // check if the given user-uuid exists in db
        if (!userDAO.existsByUUIDAndRole(dto.getUserUUID(), User.Role.BUSINESS.name())) {
            throw new EntityNotFoundException(String.format("User with UUID: %s does not exist as business entity", dto.getUserUUID()));
        }
        if (businessDAO.findByUserUUID(dto.getUserUUID()) != null) {
            throw new BusinessError(new ErrorMessage("business.exists"));
        }

        Business business = new Business();
        business.prepareForCreate();
        this.businessConverter.toEntity(dto, business);

        GeoJsonPoint geoJsonPoint = geoCodingService.geoCode(dto.getAddress().generateString());
        business.getAddress().setGpsLocation(geoJsonPoint);

        LOGGER.trace("Creating business");
        return this.businessConverter.toDTO(businessDAO.save(business));

    }

    /**
     * Update existing business
     * @param dto
     * @return
     */
    public BusinessDTO updateBusiness(BusinessDTO dto) {

        Business business = businessDAO.findByUserUUID(dto.getUserUUID());
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", dto.getUserUUID()));
        }

        business.prepareForUpdate();
        this.businessConverter.toEntity(dto, business);

        LOGGER.trace(String.format("Updating business for user with UUID : %s", dto.getUserUUID()));

        return businessConverter.toDTO(businessDAO.update(business));

    }

    /**
     * Retrieve business for user
     * @param userUUID
     * @return
     */
    public BusinessDTO getBusiness(String userUUID) {
        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }

        LOGGER.trace(String.format("Retrieve business for user with UUID : %s", userUUID));

        return businessConverter.toDTO(business);
    }

    /**
     * Save a resized version of the logo
     * @param userUUID
     * @param contentType
     * @param logo
     * @return
     */
    public byte[] saveLogo(String userUUID, String contentType, byte[] logo){
        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }

        if (contentType != null
                && !contentType.equalsIgnoreCase(MediaType.IMAGE_GIF_VALUE)
                && !contentType.equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE)
                && !contentType.equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE)) {

            List<ErrorModel> errors = new ArrayList<>();
            errors.add(new ErrorModel("business.logo.content.type"));

            throw new ValidationError(errors);
        }

        business.setLogo(ImageUtil.resizeImage(logo, ImageUtil.Size.M, contentType));
        business.setLogoContentType(contentType);

        businessDAO.update(business);

        return business.getLogo();
    }


    /**
     * Save picture for a business
     * @param userUUID
     * @param dto
     * @return
     */
    public FileResourceDTO savePicture(String userUUID, FileResourceDTO dto) {

        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }

        FileResourceDTO picture = fileResourceService.saveFileResource(dto);

        if  (business.getPictures() == null) {
            business.setPictures(new ArrayList<String>());
        }

        business.getPictures().add(picture.getUUID());
        businessDAO.update(business);

        LOGGER.trace(String.format("Save picture for business of user uuid : %s", userUUID));

        return picture;
    }

    /**
     * Delete picture of a business
     * @param userUUID
     * @param picUUID
     */
    public void deletePicture(String userUUID, String picUUID) {

        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }

        business.getPictures().removeIf(p -> p.equalsIgnoreCase(picUUID));

        businessDAO.update(business);

        fileResourceService.deleteFileResource(picUUID);
    }

    /**
     * Add carehome for a business
     * @param userUUID
     * @param dto
     * @return
     */
    public PointOfInterestDTO createCareHome(String userUUID, PointOfInterestDTO dto) {
        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }

        dto.setBusinessUUID(business.getUUID());
        PointOfInterestDTO careHome = careHomeService.createPOI(dto);

        return careHome;
    }

    /**
     * Retrieve all carehomes for a business
     * @param userUUID
     * @return
     */
    public Collection<PointOfInterestDTO> getCareHomes(String userUUID) {
        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }

        return careHomeService.retrieveByBusinessUUID(business.getUUID());
    }

    /**
     * Delete carehome
     * @param userUUID
     * @param poiUUID
     */
    public void deleteCareHome(String userUUID, String poiUUID) {
        checkRelationShip(userUUID, poiUUID);
        careHomeService.deletePOI(poiUUID);
    }

    /**
     * Update carehome
     * @param userUUID
     * @param poiUUID
     * @param dto
     * @return
     */
    public PointOfInterestDTO updateCareHome(String userUUID, String poiUUID, PointOfInterestDTO dto) {
        Business business = checkRelationShip(userUUID, poiUUID);

        dto.setBusinessUUID(business.getUUID());
        return  careHomeService.updatePOI(poiUUID, dto);
    }


    public FileResourceDTO saveCareHomePicture(String userUUID, String poiUUID, FileResourceDTO fileResourceDTO, boolean firstPicture) {
        checkRelationShip(userUUID, poiUUID);
        return careHomeService.savePicture(poiUUID, fileResourceDTO, firstPicture);
    }

    public Collection<FileResourceDTO> getCareHomePictures(String userUUID, String poiUUID) {
        checkRelationShip(userUUID, poiUUID);

        return careHomeService.getPictures(poiUUID);
    }

    public void deleteCareHomePicture(String userUUID, String poiUUID, String picUUID) {
        checkRelationShip(userUUID, poiUUID);

        careHomeService.deletePicture(poiUUID, picUUID);
    }

    /**
     * Chekc if the given carehome belongs to a certain business
     * @param userUUID
     * @param poiUUID
     * @return
     */
    private Business checkRelationShip(String userUUID, String poiUUID) {
        Business business = businessDAO.findByUserUUID(userUUID);
        if (business == null) {
            throw new EntityNotFoundException(String.format("Business does not exists for user with uuid : %s", userUUID));
        }
        if (!careHomeService.existsByBusinessUUIDAndPOIUUID(business.getUUID(), poiUUID)) {
            throw new EntityNotFoundException(String.format("Carehome does not exists for business with uuid : %s", business.getUUID()));
        }
        return business;
    }


    public PointOfInterestDTO getCarehome(String userUUID, String poiUUID) {
        checkRelationShip(userUUID, poiUUID);

        return careHomeService.getPOI(poiUUID);
    }

    /** Get all businesses
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PaginatedResult<BusinessDTO> retrieveAll(int pageNumber, int pageSize) {

        // retrieve businesses
        Collection<Business> businesses = businessDAO.findAll(pageNumber, pageSize).getPayload();

        List<String> buUUIDS = new ArrayList<>();

        // get all businesses ids
        businesses.forEach(business -> buUUIDS.add(business.getUUID()));

        // retrieve number of carehomes for each business
        Map<String, Integer> numberOfCarehomes = businessDAO.getNumberOfCarehomes(buUUIDS);

        List<BusinessDTO> businessDTOs = businessConverter.toDTOList(businesses);

        // set the number of carehomes for every business
        businessDTOs.forEach(dto -> dto.setCarehomes(numberOfCarehomes.get(dto.getUUID())));

        return new PaginatedResult<>(businessDTOs, 0, 0 ,0);
    }
}

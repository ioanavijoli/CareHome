package com.servustech.carehome.poi;

import com.servustech.carehome.comment.CommentDAO;
import com.servustech.carehome.file.FileResourceDTO;
import com.servustech.carehome.file.FileResourceService;
import com.servustech.carehome.google.maps.GeoCodingService;
import com.servustech.carehome.persistence.model.PointOfInterest;
import com.servustech.carehome.util.ImageUtil;
import com.servustech.carehome.util.exception.BusinessError;
import com.servustech.carehome.util.exception.ErrorMessage;
import com.servustech.mongo.utils.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;

@Service
public class POIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(POIService.class);

	private final POIDAO careHomeDAO;

	private final POIConverter poiConverter;

	private final POIValidator poiValidator;

	private final FileResourceService fileResourceService;

	private final CommentDAO commentDAO;

	private final GeoCodingService geoCodingService;

	@Autowired
	public POIService(final POIDAO careHomeDAO, final POIConverter poiConverter, POIValidator poiValidator,
					  FileResourceService fileResourceService, CommentDAO commentDAO, GeoCodingService geoCodingService) {
		this.careHomeDAO = careHomeDAO;
		this.poiConverter = poiConverter;
		this.poiValidator = poiValidator;
		this.fileResourceService = fileResourceService;
		this.commentDAO = commentDAO;
		this.geoCodingService = geoCodingService;
	}

	/**
	 * Create new point of interest
	 * 
	 * @param dto
	 * @return
	 */
	public PointOfInterestDTO createPOI(PointOfInterestDTO dto) {
		this.poiValidator.validate(dto);

		if (careHomeDAO.existsByNameOrEmail(dto.getName(), dto.getEmail())){
			throw new BusinessError(new ErrorMessage("poi.already.exists"));
		}

		PointOfInterest poi = new PointOfInterest();
		poi.prepareForCreate();
		this.poiConverter.toEntity(dto, poi);

		GeoJsonPoint geoJsonPoint = geoCodingService.geoCode(poi.getAddress().generateString());
		poi.getAddress().setGpsLocation(geoJsonPoint);

		LOGGER.trace("Creating point of interest");
		return poiConverter.toDTO(careHomeDAO.save(poi));
	}

	/**
	 * Update point of interest
	 * 
	 * @param poiUUID
	 * @param dto
	 * @return
	 */
	public PointOfInterestDTO updatePOI(String poiUUID, PointOfInterestDTO dto) {
		poiValidator.validate(dto);

		PointOfInterest poi = careHomeDAO.findByUUID(poiUUID);
		poi.prepareForUpdate();

		// if the address fields are changed then also the gps location is different
		if (!poi.getAddress().equals(dto.getAddress()) || poi.getAddress().getGpsLocation() == null) {
			dto.getAddress().setGpsLocation(geoCodingService.geoCode(dto.getAddress().generateString()));
		}

		poiConverter.toEntity(dto, poi);

		LOGGER.trace(String.format("Creating point of interest with UUID : {}", poiUUID));

		return poiConverter.toDTO(careHomeDAO.update(poi));
	}

	/**
	 * Retrieve point of interest
	 * 
	 * @param poiUUID
	 * @return
	 */
	public PointOfInterestDTO getPOI(String poiUUID) {
		PointOfInterest poi = careHomeDAO.findByUUID(poiUUID);
		if (poi == null) {
			throw new EntityNotFoundException(String.format("Point of interest with UUID: %s does not exist", poiUUID));
		}

		LOGGER.trace(String.format("Retrieving point of interest with UUID : {}", poiUUID));

		return poiConverter.toDTO(poi);
	}

	/**
	 * Remove point of interest
	 * 
	 * @param poiUUID
	 */
	public void deletePOI(String poiUUID) {
		LOGGER.trace(String.format("Deleting point of interest with UUID : {}", poiUUID));
		PointOfInterest poi = careHomeDAO.findByUUID(poiUUID);

		// make sure to delete all the pictures of the poi
		if (poi.getPictures() != null) {
			fileResourceService.deleteByUUIDs(poi.getPictures());
		}

		careHomeDAO.delete(poiUUID);

	}

	/**
	 * Retrieve all point of interest but paginated
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Collection<PointOfInterestDTO> retrieveAll(int pageNumber, int pageSize) {
		Collection<PointOfInterest> carehomes = careHomeDAO.findAll(pageNumber, pageSize).getPayload();
		return getCareHomeDTOList(carehomes);
	}
	/**
	 * Retrieve point of interests by a list of given UUIDs but paginated
	 *
	 * @param uuids
	 * @return
	 */
	public Collection<PointOfInterestDTO> retrieveByUUIDList(PointOfInterestUUID uuids) {
		Set<String> uuidsSet = new LinkedHashSet<>();
		uuids.getUuids().iterator().forEachRemaining(uuid -> uuidsSet.add(uuid));

		Collection<PointOfInterest> carehomes = careHomeDAO.findByUUIDs(uuidsSet);
		checkForFirstImage(carehomes);

		// calculate the rating for the given set of carehomes
		Map<String, Double> ratings = commentDAO.calculateRating(uuidsSet);
		Map<String, Double> priceRatings = commentDAO.calculatePriceRating(uuidsSet);

		Map<String, PointOfInterestDTO> carehomesMap = new HashMap<>();
		List<PointOfInterestDTO> carehomeDTOs = this.poiConverter.toDTOList(carehomes);

		carehomeDTOs.forEach(carehome -> {
			if  (Objects.nonNull(ratings.get(carehome.getUUID()))) {
				carehome.setRating(ratings.get(carehome.getUUID()));
			}
			if (Objects.nonNull(priceRatings.get(carehome.getUUID()))) {
				carehome.setPriceRating(priceRatings.get(carehome.getUUID()));
			}
			carehomesMap.put(carehome.getUUID(), carehome);
		});

		List<PointOfInterestDTO> keepCareHomeQueryOderList = new LinkedList<>();
		uuidsSet.iterator().forEachRemaining(uuid -> keepCareHomeQueryOderList.add(carehomesMap.get(uuid)));

		return keepCareHomeQueryOderList;
	}

	public Collection<PointOfInterestDTO> compareByPrice(PointOfInterestUUID uuids) {
		Collection<PointOfInterest> carehomes = careHomeDAO.findByUUIDs(uuids.getUuids());
		checkForFirstImage(carehomes);

		// calculate the rating for the given set of carehomes
		Map<String, Double> ratings = commentDAO.calculateRating(uuids.getUuids());
		Map<String, Double> priceRatings = commentDAO.calculatePriceRating(uuids.getUuids());

		Map<String, PointOfInterestDTO> carehomesMap = new HashMap<>();
		List<PointOfInterestDTO> carehomeDTOs = this.poiConverter.toDTOList(carehomes);

		carehomeDTOs.forEach(carehome -> {
			if  (Objects.nonNull(ratings.get(carehome.getUUID()))) {
				carehome.setRating(ratings.get(carehome.getUUID()));
			}
			if (Objects.nonNull(priceRatings.get(carehome.getUUID()))) {
				carehome.setPriceRating(priceRatings.get(carehome.getUUID()));
			}
			carehomesMap.put(carehome.getUUID(), carehome);
		});

		carehomeDTOs.sort((p1, p2) -> Double.compare(p1.getRating(), p2.getRating()));
		Collections.reverse(carehomeDTOs);

		for (int i = 0; i < carehomeDTOs.size() - 1; i++) {
			PointOfInterestDTO carehome1 = carehomeDTOs.get(i);
			PointOfInterestDTO carehome2 = carehomeDTOs.get(i + 1);

			int start = i;
			int end = start;
			while (carehome1.getRating() == carehome2.getRating() && i < carehomeDTOs.size() - 1) {
				i++;
				end++;
				if (i < carehomeDTOs.size() - 1) {
					carehome1 = carehomeDTOs.get(i);
					carehome2 = carehomeDTOs.get(i + 1);
				}
			}

			if (start != end) {
				carehomeDTOs.subList(start, end + 1).sort((p1, p2) -> Integer.compare(p1.getPrice(), p2.getPrice()));
			}
		}

		return carehomeDTOs;
	}

	/**
	 * Save picture for a given point of interest
	 *
	 * @param poiUUID
	 * @param fileResourceDTO
	 * @return
	 */
	public FileResourceDTO savePicture(String poiUUID, FileResourceDTO fileResourceDTO, boolean firstPicture) {

		LOGGER.trace(String.format("Save picture for point of interest with UUID : {}", poiUUID));

		FileResourceDTO picture = fileResourceService.saveFileResource(fileResourceDTO);

		PointOfInterest careHome = careHomeDAO.findByUUID(poiUUID);

		if (careHome.getPictures() == null) {
			careHome.setPictures(new ArrayList<String>());
		}

		if (firstPicture) {
			careHome.setFirstPicture(ImageUtil.resizeImage(fileResourceDTO.getContent(), ImageUtil.Size.M, fileResourceDTO.getContentType()));
		}

		careHome.getPictures().add(picture.getUUID());

		careHomeDAO.update(careHome);
		return picture;
	}

	/**
	 * Return a list of UUIDs
	 *
	 * @param poiUUID
	 * @return
	 */
	public Collection<FileResourceDTO> getPictures(String poiUUID) {
		LOGGER.trace(String.format("Get pictures for point of interest with UUID : {}", poiUUID));

		PointOfInterest poi = careHomeDAO.findByUUID(poiUUID);

		if (poi.getPictures() != null) {
			return fileResourceService.getFilesByUUIDs(poi.getPictures());
		} else {
			return Collections.emptyList();
		}

	}

	public Collection<String> getPicturesUUIDs(String poiUUID) {
		LOGGER.trace(String.format("Get pictures for point of interest with UUID : {}", poiUUID));

		PointOfInterest poi = careHomeDAO.findByUUID(poiUUID);

		return poi.getPictures() != null ? poi.getPictures() : Collections.emptyList();
	}

	/**
	 * Delete picture for a given point of interest
	 *
	 * @param poiUUID
	 * @param picUUID
	 */
	public void deletePicture(String poiUUID, String picUUID) {
		LOGGER.trace(String.format("Delete picture for point of interest with UUID : {}", poiUUID));
		PointOfInterest poi = careHomeDAO.findByUUID(poiUUID);

		poi.getPictures().removeIf(p -> p.equalsIgnoreCase(picUUID));

		careHomeDAO.update(poi);

		fileResourceService.deleteFileResource(picUUID);

	}

	/**
	 * Retrieve all carehomes of an business
	 * @param uuid
	 * @return
     */
	public Collection<PointOfInterestDTO> retrieveByBusinessUUID(String uuid) {

		Collection<PointOfInterest> carehomes = careHomeDAO.findAllByBusinessUUID(uuid);

		checkForFirstImage(carehomes);

		return poiConverter.toDTOList(carehomes);
	}

	public Collection<PointOfInterestDTO> retrieveByName(String careHomeName, int pageNumber, int pageSize) {
		Collection<PointOfInterest> carehomes = careHomeDAO.findByName(this.toTrimString(careHomeName),
																  pageNumber, pageSize);
		return getCareHomeDTOList(carehomes);
	}

	public Collection<PointOfInterestDTO> retrieveByAddress(Integer zipCode, Integer zipCodeRange,
													  String country, String city, String street,
													  int pageNumber, int pageSize) {
		Collection<PointOfInterest> carehomes = careHomeDAO.findByAddress(zipCode, zipCodeRange,
																	 this.toTrimString(country),
																	 this.toTrimString(city),
																	 this.toTrimString(street),
																	 pageNumber, pageSize);
		return getCareHomeDTOList(carehomes);
	}

	public Collection<PointOfInterestDTO> retrieveByRegion(String postalCode,
													 int pageNumber, int pageSize) {
		String region = postalCode.trim().substring(0, postalCode.indexOf(" "));
		Collection<PointOfInterest> carehomes = careHomeDAO.findByRegion(region.toLowerCase(), pageNumber, pageSize);
		return getCareHomeDTOList(carehomes);
	}

	public Collection<PointOfInterestDTO> retrieveByRatingInterval(Double minValue, Double maxValue, int pageNumber, int pageSize) {
		Map<String, Double> ratings = commentDAO.calculateRating(minValue, maxValue, pageNumber, pageSize);

		Collection<PointOfInterest> carehomes = careHomeDAO.findByUUIDs(ratings.keySet());
		checkForFirstImage(carehomes);

		List<PointOfInterestDTO> carehomeDTOs = this.poiConverter.toDTOList(carehomes);

		carehomeDTOs.stream()
					.filter(carehome -> Objects.nonNull(ratings.get(carehome.getUUID())))
					.forEach(carehome -> carehome.setRating(ratings.get(carehome.getUUID())));

		return carehomeDTOs;
	}

	public Collection<PointOfInterestDTO> retrieveByPriceInterval(Double minValue, Double maxValue, int pageNumber, int pageSize) {
		Collection<PointOfInterest> carehomes = careHomeDAO.findByPrice(minValue, maxValue, pageNumber, pageSize);
		return getCareHomeDTOList(carehomes);
	}

	/**
	 * Check if there is a first picture set if not set one from the given list
	 * @param carehomes
	 * @return
     */
	private Collection<PointOfInterest> checkForFirstImage(Collection<PointOfInterest> carehomes) {
		Predicate<List<String>> isEmpty = CollectionUtils::isEmpty;
		for (PointOfInterest carehome : carehomes) {
			if (carehome.getFirstPicture() == null && !isEmpty.test(carehome.getPictures())) {
				FileResourceDTO pic = fileResourceService.getFileResourceByUUID(carehome.getPictures().get(0));
				byte[] firstPicture = ImageUtil.resizeImage(pic.getContent(), ImageUtil.Size.M, pic.getContentType());
				carehome.setFirstPicture(firstPicture);

				careHomeDAO.update(carehome);
			}
		}

		return carehomes;
	}


	/**
	 * check if a carehome exists for the given business
	 * @param busUUID
	 * @param poiUUID
     * @return
     */
	public boolean existsByBusinessUUIDAndPOIUUID(String busUUID, String poiUUID) {
		return careHomeDAO.existsByBusinessUUIDAndPOIUUID(busUUID, poiUUID);
	}


	private Collection<PointOfInterestDTO> getCareHomeDTOList(Collection<PointOfInterest> carehomes) {
		checkForFirstImage(carehomes);

		Set<String> uuidSet = new HashSet<>();
		carehomes.forEach(carehome -> uuidSet.add(carehome.getUUID()));

		// calculate the rating for the given set of carehomes
		Map<String, Double> ratings = commentDAO.calculateRating(uuidSet);

		List<PointOfInterestDTO> carehomeDTOs = this.poiConverter.toDTOList(carehomes);
		carehomeDTOs.stream()
					.filter(carehome -> Objects.nonNull(ratings.get(carehome.getUUID())))
					.forEach(carehome -> carehome.setRating(ratings.get(carehome.getUUID())));

		return carehomeDTOs;
	}

	private String toTrimString(String str) {
		return  Objects.isNull(str) ? null : str.trim().toLowerCase();
	}

}

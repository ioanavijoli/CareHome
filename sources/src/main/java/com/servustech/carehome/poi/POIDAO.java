package com.servustech.carehome.poi;

import com.servustech.carehome.persistence.model.POIAddress;
import com.servustech.carehome.persistence.model.PointOfInterest;
import com.servustech.carehome.persistence.model.PointOfInterest_;
import com.servustech.mongo.utils.PaginatedResult;
import com.servustech.mongo.utils.dao.impl.EntityDAOImpl;
import com.servustech.mongo.utils.model.BaseEntity_;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
public class POIDAO extends EntityDAOImpl<ObjectId, PointOfInterest> {

	public static final String CASE_INS_OPT = "i";
	public static final String ADDRESS_POSTAL_CODE = "address.postalCode";
	public static final String ADDRESS_COUNTRY = "address.country";
	public static final String ADDRESS_CITY = "address.city";
	public static final String ADDRESS_STREET = "address.street";

	public POIDAO() {
		super(PointOfInterest.class);
	}

	public Collection<PointOfInterest> findAllByBusinessUUID(String busUUID) {
		final Query query = new Query(Criteria.where(PointOfInterest_.FIELD_BUSINESS_UUID).is(busUUID));
		return this.mongoOperations.find(query, PointOfInterest.class);
	}

	public boolean existsByBusinessUUIDAndPOIUUID(String busUUID, String poiUUID) {
		final Query query = new Query(Criteria.where(PointOfInterest_.FIELD_BUSINESS_UUID).is(busUUID).
				andOperator(Criteria.where(BaseEntity_.FIELD_UUID).is(poiUUID)));
		return this.mongoOperations.exists(query, PointOfInterest.class);
	}

	public boolean existsByNameOrEmail(String name, String email) {
		Criteria criteria = new Criteria();
		Criteria nameCrit = Criteria.where(PointOfInterest_.FIELD_NAME).regex("^" + name + "$", CASE_INS_OPT);
		Criteria emailCrit = Criteria.where(PointOfInterest_.FIELD_EMAIL).is(email);
		criteria.orOperator(nameCrit, emailCrit);
		return this.mongoOperations.exists(new Query(criteria), PointOfInterest.class);
	}

	public Collection<PointOfInterest> findByName(String name, int pageNumber, int pageSize) {
		final Criteria criteria = Criteria.where(PointOfInterest_.FIELD_NAME).regex(name, CASE_INS_OPT);
		Query query = new Query(criteria).skip((pageNumber - 1) * pageSize).limit(pageSize);
		return this.mongoOperations.find(query, PointOfInterest.class);
	}

	public Collection<PointOfInterest> findByAddress(Integer zipCode, Integer zipCodeRange,
														  String country, String city, String street,
														  int pageNumber, int pageSize) {
		boolean searchByZipCode = Objects.nonNull(zipCode) && Objects.nonNull(zipCodeRange) && Objects.isNull(country);
		boolean searchByLocation = Objects.isNull(zipCode) && Objects.nonNull(country);
		boolean searchByAddress = Objects.nonNull(zipCode) && Objects.nonNull(zipCodeRange) && Objects.nonNull(country);

		Criteria criteria = new Criteria();
		if (searchByZipCode) {
			criteria = Criteria.where(ADDRESS_POSTAL_CODE).in(generateZipCodeValues(zipCode, zipCodeRange));
		} else if (searchByLocation) {
			criteria = getSearchCriteria(country, city, street);
		} else if (searchByAddress) {
			criteria = getSearchCriteria(country, city, street)
					.and(ADDRESS_POSTAL_CODE).in(generateZipCodeValues(zipCode, zipCodeRange));
		}

		final Query query = new Query(criteria).skip((pageNumber - 1) * pageSize).limit(pageSize);
		return this.mongoOperations.find(query, PointOfInterest.class);
	}

	public Collection<PointOfInterest> findByRegion(String region, int pageNumber, int pageSize) {
	    final Criteria criteria = Criteria.where(ADDRESS_POSTAL_CODE).regex(region, CASE_INS_OPT);
        final Query query = new Query(criteria).skip((pageNumber - 1) * pageSize).limit(pageSize);
        return this.mongoOperations.find(query, PointOfInterest.class);
    }

	public Collection<PointOfInterest> findByPrice(Double minValue, Double maxValue,
												   Integer pageNumber, Integer pageSize) {
		final Criteria criteria = Criteria.where(PointOfInterest_.FIELD_PRICE).gte(minValue).lte(maxValue);
		final Query query = new Query(criteria).skip((pageNumber - 1) * pageSize).limit(pageSize);
		return this.mongoOperations.find(query, PointOfInterest.class);
	}

	private Criteria getSearchCriteria(String country, String city, String street) {
		Criteria criteria = new Criteria();
		if (Objects.isNull(city) && Objects.isNull(street)) {
			criteria = Criteria.where(ADDRESS_COUNTRY).regex(country, CASE_INS_OPT);
		} else if (Objects.nonNull(city) && Objects.isNull(street)) {
			criteria = Criteria.where(ADDRESS_COUNTRY).regex(country, CASE_INS_OPT)
							   .and(ADDRESS_CITY).regex(city, CASE_INS_OPT);
		} else if (Objects.nonNull(city) && Objects.nonNull(street)) {
			criteria = Criteria.where(ADDRESS_COUNTRY).regex(country, CASE_INS_OPT)
							   .and(ADDRESS_CITY).regex(city, CASE_INS_OPT)
							   .and(ADDRESS_STREET).regex(street.toLowerCase(), CASE_INS_OPT);
		}

		return criteria;
	}

	private List<String> generateZipCodeValues(Integer zipCode, Integer zipCodeRange) {
		List<String> zipCodeValues = new ArrayList<>();
		for (int i = zipCode - zipCodeRange; i <= zipCode + zipCodeRange; i++) {
			zipCodeValues.add(String.valueOf(i));
		}

		return zipCodeValues;
	}

}

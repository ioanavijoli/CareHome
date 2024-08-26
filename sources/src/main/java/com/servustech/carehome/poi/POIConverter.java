package com.servustech.carehome.poi;

import com.servustech.carehome.persistence.model.PointOfInterest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class POIConverter {

	public PointOfInterestDTO toDTO(PointOfInterest poi) {
		if (poi == null) {
			return null;
		}
		PointOfInterestDTO dto = new PointOfInterestDTO();
		dto.setUUID(poi.getUUID());
		dto.setName(poi.getName());
		dto.setEmail(poi.getEmail());
		dto.setDescription(poi.getDescription());
		dto.setContactPerson(poi.getContactPerson());
		dto.setPhoneNumber(poi.getPhoneNumber());
		dto.setAddress(poi.getAddress());
		dto.setBusinessUUID(poi.getBusinessUUID());
		dto.setPictures(poi.getPictures());
		dto.setServices(poi.getServices());
		dto.setFirstPicture(poi.getFirstPicture());
		dto.setPrice(poi.getPrice());

		return dto;
	}

	public List<PointOfInterestDTO> toDTOList(final Collection<PointOfInterest> pois) {
		return pois.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public void toEntity(PointOfInterestDTO dto, PointOfInterest poi) {
		poi.setName(dto.getName());
		poi.setEmail(dto.getEmail());
		poi.setDescription(dto.getDescription());
		poi.setContactPerson(dto.getContactPerson());
		poi.setPhoneNumber(dto.getPhoneNumber());
		poi.setAddress(dto.getAddress());
		poi.setBusinessUUID(dto.getBusinessUUID());
		poi.setServices(dto.getServices());
		poi.setPrice(dto.getPrice());
	}

}

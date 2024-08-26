package com.servustech.carehome.business;

import com.servustech.carehome.persistence.model.Business;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessConverter {

    public BusinessDTO toDTO(Business entity) {
        BusinessDTO dto = new BusinessDTO();
        dto.setUserUUID(entity.getUserUUID());
        dto.setAddress(entity.getAddress());
        dto.setName(entity.getName());
        dto.setPictures(entity.getPictures());
        dto.setServices(entity.getServices());
        dto.setWebsite(entity.getWebsite());
        dto.setUUID(entity.getUUID());
        dto.setLogo(entity.getLogo());

        return dto;
    }

    public List<BusinessDTO> toDTOList(final Collection<Business> businesses) {
        return businesses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void toEntity(BusinessDTO dto, Business entity) {
        entity.setUserUUID(dto.getUserUUID());
        entity.setAddress(dto.getAddress());
        entity.setName(dto.getName());
        entity.setPictures(dto.getPictures());
        entity.setServices(dto.getServices());
        entity.setWebsite(dto.getWebsite());
    }

}

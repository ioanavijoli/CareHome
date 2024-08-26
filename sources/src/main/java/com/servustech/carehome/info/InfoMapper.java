package com.servustech.carehome.info;

import java.util.List;
import java.util.stream.Collectors;

public class InfoMapper {

    public static Info toEntity(InfoDTO dto) {
        Info info = new Info();
        info.setOrgID(dto.getOrgID());
        info.setName(dto.getName());
        info.setEmail(dto.getEmail());
        info.setDescription(dto.getDescription());
        info.setPhoneNumber(dto.getPhoneNumber());
        info.setAddress(AddressMapper.toEntity(dto.getAddress()));
        info.setServices(dto.getServices());
        info.setImages(dto.getImages());
        return info;
    }

    public static InfoDTO toDTO(Info info) {
        InfoDTO dto = new InfoDTO();
        dto.setId(info.getId());
        dto.setOrgID(info.getOrgID());
        dto.setName(info.getName());
        dto.setEmail(info.getEmail());
        dto.setDescription(info.getDescription());
        dto.setPhoneNumber(info.getPhoneNumber());
        dto.setAddress(AddressMapper.toDTO(info.getAddress()));
        dto.setServices(info.getServices());
        dto.setImages(info.getImages());
        return dto;
    }

    public static List<InfoDTO> toDTOList(List<Info> infos) {
        return infos.stream().map(InfoMapper::toDTO).collect(Collectors.toList());
    }
}

package com.servustech.carehome.info;

public class AddressMapper {

    public static Address toEntity(AddressDTO dto) {
        Address address = new Address();
        address.setCountry(dto.getCountry());
        address.setState(dto.getState());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setCounty(dto.getCounty());
        return address;
    }

    public static AddressDTO toDTO(Address address) {
        if (address == null) {

            // OR
            // DTO with null values
            AddressDTO dto = new AddressDTO();
            dto.setCountry(null);
            dto.setState(null);
            dto.setCity(null);
            dto.setPostalCode(null);
            dto.setStreet(null);
            dto.setNumber(null);
            dto.setCounty(null);
            return dto;

        }
        // If address is not null, map its properties to AddressDTO
        AddressDTO dto = new AddressDTO();
        dto.setCountry(address.getCountry());
        dto.setState(address.getState());
        dto.setCity(address.getCity());
        dto.setPostalCode(address.getPostalCode());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setCounty(address.getCounty());
        return dto;
    }


}

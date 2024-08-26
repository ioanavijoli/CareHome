package com.servustech.carehome.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountyService {

    @Autowired
    private CountyRepository countyRepository;

    public List<CountyDTO> getAllCounties() {
        List<County> counties = countyRepository.findAll();
        return counties.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CountyDTO convertToDTO(County countyEntity) {
        CountyDTO countyDTO = new CountyDTO();
        countyDTO.setCounty(countyEntity.getCounty());
        countyDTO.setCities(
                countyEntity.getCities().stream()
                        .map(city -> {
                            CountyDTO.CityDTO cityDTO = new CountyDTO.CityDTO();
                            cityDTO.setName(city.getName());
                            cityDTO.setCount(city.getCount());
                            return cityDTO;
                        })
                        .collect(Collectors.toList())
        );
        return countyDTO;
    }
}

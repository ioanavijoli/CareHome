package com.servustech.carehome.info;

import com.servustech.mongo.utils.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InfoService {

    private final InfoRepository repository;
    private final AddressRepository addressRepository;

    @Autowired
    public InfoService(InfoRepository infoRepository, AddressRepository addressRepository) {
        this.repository = infoRepository;
        this.addressRepository = addressRepository;
    }


    public InfoDTO create(InfoDTO dto) {
        Info info = InfoMapper.toEntity(dto);
        info = repository.save(info);
        return InfoMapper.toDTO(info);
    }

    public InfoDTO update(String id, InfoDTO dto) {
        Info info = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Info not fou\n" +
                        "        info.setOrgID(dto.getOrgID());\n" +
                        "        info.setEmail(dto.getEmail());\n" +
                        "        info.setDescription(dto.getDescription());\n" +
                        "        info.setPhoneNumber(dto.getPhoneNumber());\n" +
                        "        info.setAddress(AddressMapper.toEntity(dto.getAddress()));\n" +
                        "        info.setServices(dto.getServices());\n" +
                        "        info.setImages(dto.getImages());nd with id: " + id));
        info.setName(dto.getName());
        info = repository.save(info);
        return InfoMapper.toDTO(info);
    }

    public InfoDTO getById(String id) {
        Info info = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Info not found with id: " + id));
        return InfoMapper.toDTO(info);
    }

    public List<InfoDTO> getAll() {
        List<Info> infos = repository.findAll();
        return InfoMapper.toDTOList(infos);
    }

    public void delete(String id) {
        Info info = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Info not found with id: " + id));
        repository.delete(info);
    }

    public Page<InfoDTO> searchByCity(String city, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return repository.findByAddressCity(city, pageRequest).map(InfoMapper::toDTO);
    }

    public Page<InfoDTO> searchByLocation(String query, int page, int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);

        Page<Info> cityResults = repository.findByAddressCityStartingWith(query, pageable);
        Page<Info> countryResults = repository.findByAddressCountryStartingWith(query, pageable);
        Page<Info> regionResults = repository.findByAddressStateStartingWith(query, pageable);
        Page<Info> postalCodeResults = repository.findByAddressPostalCodeStartingWith(query, pageable);

        List<Info> combinedResults = new ArrayList<>();
        combinedResults.addAll(cityResults.getContent());
        combinedResults.addAll(countryResults.getContent());
        combinedResults.addAll(regionResults.getContent());
        combinedResults.addAll(postalCodeResults.getContent());

        int totalSize = (int) (cityResults.getTotalElements() + countryResults.getTotalElements() +
                regionResults.getTotalElements() + postalCodeResults.getTotalElements());

        List<InfoDTO> dtos = InfoMapper.toDTOList(combinedResults);

        return new PageImpl<>(dtos, pageable, totalSize);
    }


    public Page<InfoDTO> searchByNameAndType(String name, String type, int page, int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        Page<Info> results;
        if (name == null || name.isEmpty()) {
            results = repository.findByServicesContaining(type, pageable);
        } else {
            results = repository.findByNameContainingAndServicesContaining(name, type, pageable);
        }
        return results.map(InfoMapper::toDTO);
    }
    public Page<InfoDTO> searchByLocationAndType(String location, String type, int page, int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        List<Info> combinedResults = new ArrayList<>();
        int totalSize = 0;

        if (type == null || type.isEmpty()) {
            Page<Info> cityResults = repository.findByAddressCityStartingWith(location, pageable);
            Page<Info> countryResults = repository.findByAddressCountryStartingWith(location, pageable);
            Page<Info> stateResults = repository.findByAddressStateStartingWith(location, pageable);
            Page<Info> postalCodeResults = repository.findByAddressPostalCodeStartingWith(location, pageable);

            if (cityResults != null) {
                combinedResults.addAll(cityResults.getContent());
                totalSize += (int) cityResults.getTotalElements();
            }

            if (countryResults != null) {
                combinedResults.addAll(countryResults.getContent());
                totalSize += (int) countryResults.getTotalElements();
            }

            if (stateResults != null) {
                combinedResults.addAll(stateResults.getContent());
                totalSize += (int) stateResults.getTotalElements();
            }

            if (postalCodeResults != null) {
                combinedResults.addAll(postalCodeResults.getContent());
                totalSize += (int) postalCodeResults.getTotalElements();
            }
        } else {
            Page<Info> cityResults = repository.findByAddressCityContainingAndServicesContaining(location, type, pageable);
            Page<Info> countryResults = repository.findByAddressCountryContainingAndServicesContaining(location, type, pageable);
            Page<Info> stateResults = repository.findByAddressStateContainingAndServicesContaining(location, type, pageable);
            Page<Info> postalCodeResults = repository.findByAddressPostalCodeContainingAndServicesContaining(location, type, pageable);

            if (cityResults != null) {
                combinedResults.addAll(cityResults.getContent());
                totalSize += (int) cityResults.getTotalElements();
            }

            if (countryResults != null) {
                combinedResults.addAll(countryResults.getContent());
                totalSize += (int) countryResults.getTotalElements();
            }

            if (stateResults != null) {
                combinedResults.addAll(stateResults.getContent());
                totalSize += (int) stateResults.getTotalElements();
            }

            if (postalCodeResults != null) {
                combinedResults.addAll(postalCodeResults.getContent());
                totalSize += (int) postalCodeResults.getTotalElements();
            }
        }

        List<InfoDTO> dtos = InfoMapper.toDTOList(combinedResults);
        return new PageImpl<>(dtos, pageable, totalSize);
    }

    public Page<InfoDTO> searchByName(String name, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return repository.findByNameContaining(name, pageRequest).map(InfoMapper::toDTO);
    }

    public Page<InfoDTO> searchByCountry(String country, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return repository.findByAddressCountry(country, pageRequest).map(InfoMapper::toDTO);
    }

    public Page<InfoDTO> searchByPostalCode(String postalCode, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize); // Using constructor
        return repository.findByAddressPostalCode(postalCode, pageRequest).map(InfoMapper::toDTO);
    }

    public Page<InfoDTO> searchByStreet(String street, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return repository.findByAddressStreet(street, pageRequest).map(InfoMapper::toDTO);
    }

    public List<InfoDTO> findAllByIds(List<String> ids) {
        List<Info> infoList = repository.findAllByIds(ids);
        return InfoMapper.toDTOList(infoList);
    }


}

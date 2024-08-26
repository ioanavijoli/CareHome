package com.servustech.carehome.info;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InfoRepository extends MongoRepository<Info, String> {
     Optional<Info> findById(String id);
     @Query("{ '_id': { $in: ?0 } }")
     List<Info> findAllByIds(List<String> ids);
     Page<Info> findByAddressCity(String city, Pageable pageable);
     List<Info> findByAddressCity(String city);
     Page<Info> findByAddressCountry(String country, Pageable pageable);
     Page<Info> findByAddressPostalCode(String postalCode, Pageable pageable);
     Page<Info> findByAddressStreet(String street, Pageable pageable);
     Page<Info> findByNameContaining(String name, Pageable pageable);
     Page<Info> findByAddressCityStartingWith(String city, Pageable pageable);
     Page<Info> findByAddressCountryStartingWith(String country, Pageable pageable);
     Page<Info> findByAddressStateStartingWith(String state, Pageable pageable);
     Page<Info> findByAddressPostalCodeStartingWith(String postcode, Pageable pageable);
     @Query("{ 'services': { $regex: ?0, $options: 'i' } }")
     Page<Info> findByServicesContaining(String letters, Pageable pageable);
     @Query("{ $and: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'services': { $regex: ?1, $options: 'i' } } ] }")
     Page<Info> findByNameContainingAndServicesContaining(String name, String type, Pageable pageable);

     @Query("{ $and: [ { 'address.city': { $regex: ?0, $options: 'i' } }, { 'services': { $regex: ?1, $options: 'i' } } ] }")
     Page<Info> findByAddressCityContainingAndServicesContaining(String city, String type, Pageable pageable);

     @Query("{ $and: [ { 'address.country': { $regex: ?0, $options: 'i' } }, { 'services': { $regex: ?1, $options: 'i' } } ] }")
     Page<Info> findByAddressCountryContainingAndServicesContaining(String country, String type, Pageable pageable);

     @Query("{ $and: [ { 'address.state': { $regex: ?0, $options: 'i' } }, { 'services': { $regex: ?1, $options: 'i' } } ] }")
     Page<Info> findByAddressStateContainingAndServicesContaining(String state, String type, Pageable pageable);

     @Query("{ $and: [ { 'address.postalCode': { $regex: ?0, $options: 'i' } }, { 'services': { $regex: ?1, $options: 'i' } } ] }")
     Page<Info> findByAddressPostalCodeContainingAndServicesContaining(String postalCode, String type, Pageable pageable);


}

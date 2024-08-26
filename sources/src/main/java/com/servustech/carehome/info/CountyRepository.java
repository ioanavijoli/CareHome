package com.servustech.carehome.info;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountyRepository extends MongoRepository<County, String> {
}

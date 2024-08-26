package com.servustech.carehome.business;

import com.servustech.carehome.persistence.model.Business;
import com.servustech.carehome.persistence.model.Business_;
import com.servustech.carehome.persistence.model.PointOfInterest;
import com.servustech.carehome.persistence.model.PointOfInterest_;
import com.servustech.mongo.utils.dao.impl.EntityDAOImpl;
import com.servustech.mongo.utils.model.BaseEntity_;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class BusinessDAO extends EntityDAOImpl<ObjectId, Business> {
    public BusinessDAO() {
        super(Business.class);
    }

    public Business findByUserUUID(String userUUID) {
        Query query = new Query(Criteria.where(Business_.FIELD_USER_UUID).is(userUUID));
        return this.mongoOperations.findOne(query, Business.class);
    }

    public Collection<Business> getSummaryByUserUUIDs(Collection<String> userUUIDs) {
        Query query = new Query(Criteria.where(Business_.FIELD_USER_UUID).in(userUUIDs));
        query.fields().include(Business_.FIELD_NAME);
        query.fields().include(Business_.FIELD_LOGO);
        query.fields().include(Business_.FIELD_USER_UUID);

        return this.mongoOperations.find(query, Business.class);
    }

    class BusCarehome{
        @Id
        String id;
        @Field
        int total;
    }

    public Map<String, Integer> getNumberOfCarehomes(Collection<String> carehomeUUIDs) {
        TypedAggregation<PointOfInterest> aggregation = newAggregation(PointOfInterest.class,
                group(PointOfInterest_.FIELD_BUSINESS_UUID).count().as("total"),
                match(Criteria.where(BaseEntity_.FIELD_ID).in(carehomeUUIDs)));

        AggregationResults<BusCarehome> results = mongoOperations.aggregate(aggregation, BusCarehome.class);
        List<BusCarehome> busCarehomes = results.getMappedResults();

        HashMap<String, Integer> busMap = new HashMap<>();

        busCarehomes.forEach(res -> busMap.put(res.id, res.total));

        return busMap;
    }

}

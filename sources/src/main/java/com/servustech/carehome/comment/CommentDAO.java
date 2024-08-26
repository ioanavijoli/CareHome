package com.servustech.carehome.comment;

import com.mongodb.BasicDBObject;
import com.servustech.carehome.persistence.model.Comment;
import com.servustech.carehome.persistence.model.Comment_;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CommentDAO extends EntityDAOImpl<ObjectId, Comment> {

    public static final String RATING = "rating";

    public CommentDAO() { super(Comment.class);}

    public Collection<Comment> findAllByCareHomeUUID(String carehomeUUID) {
        Query query = new Query(Criteria.where(Comment_.FIELD_CAREHOME_UUID).is(carehomeUUID));
        return mongoOperations.find(query, Comment.class);
    }

    class Rating {
        @Id String id;
        @Field("rating") double rating;
    }

    class Price {
        @Id String id;
        @Field(Comment_.FIELD_PRICE_RATING) double price;
    }

    public Map<String, Double> calculateRating(Collection<String> carehomeUUIDs) {
        TypedAggregation<Comment> aggregation = newAggregation(Comment.class,
                group(Comment_.FIELD_CAREHOME_UUID).avg(Comment_.FIELD_RATING).as("rating"),
                match(Criteria.where(BaseEntity_.FIELD_ID).in(carehomeUUIDs)))
                .withOptions(Aggregation.newAggregationOptions().cursor(new BasicDBObject()).build());

        AggregationResults<Rating> results = mongoOperations.aggregate(aggregation, Rating.class);
        List<Rating> ratings = results.getMappedResults();


        HashMap<String, Double> ratingsMap = new HashMap<>();

        ratings.forEach(rating -> ratingsMap.put(rating.id, Math.round(rating.rating*100)/100.d));

        return ratingsMap;
    }

    public Map<String, Double> calculatePriceRating(Collection<String> carehomeUUIDs) {
        TypedAggregation<Comment> aggregation = newAggregation(Comment.class,
                group(Comment_.FIELD_CAREHOME_UUID).avg(Comment_.FIELD_PRICE_RATING).as(Comment_.FIELD_PRICE_RATING),
                match(Criteria.where(BaseEntity_.FIELD_ID).in(carehomeUUIDs)));

        AggregationResults<Price> results = mongoOperations.aggregate(aggregation, Price.class);
        List<Price> priceRatings = results.getMappedResults();

        HashMap<String, Double> ratingsMap = new HashMap<>();

        priceRatings.forEach(price -> ratingsMap.put(price.id, Math.round(price.price*100)/100.d));

        return ratingsMap;
    }


    public Map<String, Double> calculateRating(Double minValue, Double maxValue, Integer pageNumber, Integer pageSize) {
        TypedAggregation<Comment> aggregation = newAggregation(Comment.class,
                group(Comment_.FIELD_CAREHOME_UUID).avg(Comment_.FIELD_RATING).as(RATING),
                match(Criteria.where(RATING).gte(minValue).lte(maxValue)),
                skip((pageNumber - 1) * pageSize),
                limit(pageSize)
        );

        AggregationResults<Rating> results = mongoOperations.aggregate(aggregation, Rating.class);
        List<Rating> ratings = results.getMappedResults();

        HashMap<String, Double> ratingsMap = new HashMap<>();

        ratings.forEach(rating -> ratingsMap.put(rating.id, rating.rating));

        return ratingsMap;
    }
}

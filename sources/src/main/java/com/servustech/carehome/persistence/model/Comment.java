package com.servustech.carehome.persistence.model;

import com.github.grozandrei.exposable.annotation.Exposable;
import com.servustech.carehome.util.PersistenceConstants;
import com.servustech.mongo.utils.model.AuditedEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = PersistenceConstants.COMMENT_COLLECTION)
@Exposable
public class Comment extends AuditedEntity{

    @NotBlank
    private String comment;

    private int rating;

    private int priceRating;

    @NotBlank
    private String userUUID;
    @NotBlank
    private String carehomeUUID;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(int priceRating) {
        this.priceRating = priceRating;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getCarehomeUUID() {
        return carehomeUUID;
    }

    public void setCarehomeUUID(String carehomeUUID) {
        this.carehomeUUID = carehomeUUID;
    }
}

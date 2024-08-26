package com.servustech.carehome.comment;

import com.servustech.carehome.web.model.BaseDTO;

import java.time.LocalDateTime;

public class CommentDTO extends BaseDTO {

    private String comment;
    private int rating;
    private int priceRating;
    private String userUUID;
    private String carehomeUUID;
    /** Date when the entity was created. */
    private LocalDateTime dateCreated;

    /** Date when the entity was last updated. */
    private LocalDateTime	dateUpdated;

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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}

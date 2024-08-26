package com.servustech.carehome.comment;

import com.servustech.carehome.persistence.model.Comment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {

    public CommentDTO toDTO (Comment entity) {
        CommentDTO dto = new CommentDTO();
        dto.setUUID(entity.getUUID());
        dto.setUserUUID(entity.getUserUUID());
        dto.setCarehomeUUID(entity.getCarehomeUUID());
        dto.setComment(entity.getComment());
        dto.setRating(entity.getRating());
        dto.setPriceRating(entity.getPriceRating());
        dto.setDateCreated(entity.getDateCreated());
        dto.setDateUpdated(entity.getDateUpdated());

        return dto;
    }

    public List<CommentDTO> toDTOList(final Collection<Comment> comments) {
        return comments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void toEntity(CommentDTO dto, Comment comment) {
        comment.setUserUUID(dto.getUserUUID());
        comment.setRating(dto.getRating());
        comment.setPriceRating(dto.getPriceRating());
        comment.setCarehomeUUID(dto.getCarehomeUUID());
        comment.setComment(dto.getComment());
    }

}

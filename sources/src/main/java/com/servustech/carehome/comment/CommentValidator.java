package com.servustech.carehome.comment;

import com.servustech.carehome.poi.PointOfInterestDTO;
import com.servustech.carehome.util.Validator;
import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentValidator implements Validator<CommentDTO> {

    @Override
    public void validate(CommentDTO dto) {
        final List<ErrorModel> errors = new ArrayList<>();

        if (StringUtils.isBlank(dto.getComment())) {
            errors.add(new ErrorModel("comments.comment.not.blank"));
        }

        if (StringUtils.isBlank(dto.getUserUUID())){
            errors.add(new ErrorModel("comments.userUUID.not.blank"));
        }

        if (StringUtils.isBlank(dto.getCarehomeUUID())){
            errors.add(new ErrorModel("comments.carehomeUUID.not.blank"));
        }

        if (dto.getRating() > 5){
            errors.add(new ErrorModel("comments.rating.invalid"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationError(errors);
        }

    }
}

package com.servustech.carehome.util;

import com.servustech.carehome.util.exception.ErrorModel;
import com.servustech.carehome.util.exception.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IDinu on 12/16/2016.
 */
@Component
public class RatingValidator {

    public void validate(double min, double max) {
        final List<ErrorModel> errors = new ArrayList<>();

        if (min < 0 || min > 5) {
            errors.add(new ErrorModel("rating.min.range.exceeded"));
        }

        if (max < 0 || max > 5) {
            errors.add(new ErrorModel("rating.max.range.exceeded"));
        }

        if (min > max) {
            errors.add(new ErrorModel("rating.min.gt.max"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationError(errors);
        }
    }
}

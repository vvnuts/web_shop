package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import lombok.*;

@Getter
@Builder
public class NotFoundRelatedObjectException extends RuntimeException{
    private final ValidationErrorResponse validationErrorResponses;

    public NotFoundRelatedObjectException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

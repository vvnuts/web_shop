package com.vvnuts.shop.dtos.responses.erorrs;

import lombok.*;

@Getter
@Builder
public class NotFoundRelatedObjectException extends RuntimeException{
    private final ValidationErrorResponse validationErrorResponses;

    public NotFoundRelatedObjectException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

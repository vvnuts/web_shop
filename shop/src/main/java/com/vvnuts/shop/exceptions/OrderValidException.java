package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderValidException extends RuntimeException{
    private final ValidationErrorResponse validationErrorResponses;

    public OrderValidException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

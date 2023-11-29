package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StringAndNumValueTogetherException extends RuntimeException{
    private final ValidationErrorResponse validationErrorResponses;

    public StringAndNumValueTogetherException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

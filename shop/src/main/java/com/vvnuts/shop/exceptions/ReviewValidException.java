package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewValidException extends ValidException{
    public ReviewValidException() {
    }

    public ReviewValidException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

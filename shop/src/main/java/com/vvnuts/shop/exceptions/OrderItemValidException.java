package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemValidException extends ValidException{
    public OrderItemValidException() {
    }

    public OrderItemValidException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

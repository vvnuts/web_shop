package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;

abstract public class ValidException extends RuntimeException{
    protected ValidationErrorResponse validationErrorResponses;

    public ValidationErrorResponse getValidationErrorResponses() {
        return validationErrorResponses;
    }
}

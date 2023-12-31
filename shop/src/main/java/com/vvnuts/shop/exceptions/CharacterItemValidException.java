package com.vvnuts.shop.exceptions;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import lombok.Builder;
import lombok.Getter;

public class CharacterItemValidException extends ValidException{
    public CharacterItemValidException(ValidationErrorResponse validationErrorResponses) {
        this.validationErrorResponses = validationErrorResponses;
    }
}

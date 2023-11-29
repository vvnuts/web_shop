package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;

import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.exceptions.NotFoundRelatedObjectException;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacteristicValidator {
    private final CharacteristicRepository repository;
    private final CategoryValidator categoryValidator;

    public void validate(CharacteristicRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.getViolations().addAll(categoryValidator.isAllCategoryFound(request.getCategories()));
        if (response.getViolations().size() > 0) {
            throw new NotFoundRelatedObjectException(response);
        }
    }

    public Characteristic validate(CharacteristicRequest request, Integer id) {
        Characteristic characteristic = repository.findById(id).orElseThrow();
        validate(request);
        return characteristic;
    }
}

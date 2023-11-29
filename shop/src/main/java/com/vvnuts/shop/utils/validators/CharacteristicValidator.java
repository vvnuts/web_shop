package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;

import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacteristicValidator {
    private final CharacteristicRepository repository;
    private final CategoryValidator categoryValidator;

    public void validate(CharacteristicRequest request) {
        categoryValidator.isAllCategoryFound(request.getCategories());
    }

    public Characteristic validate(CharacteristicRequest request, Integer id) {
        Characteristic characteristic = repository.findById(id).orElseThrow();
        categoryValidator.isAllCategoryFound(request.getCategories());
        return characteristic;
    }
}

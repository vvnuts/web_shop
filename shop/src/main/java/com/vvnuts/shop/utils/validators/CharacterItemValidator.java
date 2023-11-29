package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.exceptions.StringAndNumValueTogetherException;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterItemValidator {
    private final CharacterItemRepository repository;
    private final CharacteristicRepository characteristicRepository;

    public void validate(CharacterItemRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();

        Optional<Characteristic> optional = characteristicRepository.findById(request.getCharacteristic());
        if (optional.isEmpty()) {
            response.getViolations()
                    .add(new Violation("Characteristic", "Характеристика с id "
                            + request.getCharacteristic() + " not found"));
        }

        response.getViolations().add(isStringAndNumValueTogether(request));
        if (response.getViolations().size() > 0) {
            throw new StringAndNumValueTogetherException(response);
        }
    }

    public CharacterItem validate(CharacterItemRequest request, Integer id) {
        CharacterItem characterItem = repository.findById(id).orElseThrow();
        validate(request);
        return characterItem;
    }

    public Violation isStringAndNumValueTogether(CharacterItemRequest request) {
        if (request.getNumValue() != null && request.getValue() != null) {
            return new Violation("CharacterItem",
                    "Численное и строковое значение у характеристики  c id " +
                            + request.getCharacteristic());
        }
        return null;
    }

}

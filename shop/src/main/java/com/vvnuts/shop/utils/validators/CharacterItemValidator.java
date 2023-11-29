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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterItemValidator {
    private final CharacterItemRepository repository;
    private final CharacteristicRepository characteristicRepository;

    public void validate(CharacterItemRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.getViolations().add(isCharacteristicFound(request.getCharacteristic()));
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

    public Violation isCharacteristicFound(Integer characteristicId) {
        Optional<Characteristic> optional = characteristicRepository.findById(characteristicId);
        if (optional.isEmpty()) {
            return new Violation("Characteristic", "Характеристика с id "
                            + characteristicId + " not found");
        }
        return null;
    }

    public Violation isStringAndNumValueTogether(CharacterItemRequest request) {
        if (request.getNumValue() != null && request.getValue() != null) {
            return new Violation("CharacterItem",
                    "Численное и строковое значение у характеристики  c id " + request.getCharacteristic());
        }
        return null;
    }

    public List<Violation> isListCharacterItemValid(List<CharacterItemRequest> requests) {
        List<Violation> violations = new ArrayList<>();
        for (CharacterItemRequest request: requests) {
            violations.add(isCharacteristicFound(request.getCharacteristic()));
            violations.add(isStringAndNumValueTogether(request));
        }
        return violations;
    }

}

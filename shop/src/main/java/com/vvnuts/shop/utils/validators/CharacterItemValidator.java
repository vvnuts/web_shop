package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.enums.Type;
import com.vvnuts.shop.exceptions.CharacterItemValidException;
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
        Violation characteristicFound = isCharacteristicFound(request.getCharacteristic());

        if (characteristicFound == null) {
            Violation valueComplyType = isValueComplyType(request);
            if (valueComplyType != null) {
                response.getViolations().add(valueComplyType);
            }
        } else {
            response.getViolations().add(characteristicFound);
        }

        if (!response.getViolations().isEmpty()) {
            throw new CharacterItemValidException(response);
        }
    }

    public CharacterItem validate(CharacterItemRequest request, Integer id) {
        CharacterItem characterItem = repository.findById(id).orElseThrow();
        validate(request);
        return characterItem;
    }

    private Violation isCharacteristicFound(Integer characteristicId) {
        Optional<Characteristic> optional = characteristicRepository.findById(characteristicId);
        if (optional.isEmpty()) {
            return new Violation("Characteristic", "Характеристика с id "
                            + characteristicId + " not found");
        }
        return null;
    }

    private Violation isValueComplyType(CharacterItemRequest request) {
        Characteristic characteristic = characteristicRepository.findById(request.getCharacteristic()).orElseThrow();
        if (characteristic.getType() == Type.STRING && request.getNumValue() != null) {
            return new Violation("value", "Характеристика с id "
                    + request.getCharacteristic() + " не может иметь значение типа INTEGER");
        }
        if (characteristic.getType() == Type.INTEGER && request.getValue() != null) {
            return new Violation("numValue", "Характеристика с id "
                    + request.getCharacteristic() + " не может иметь значение типа STRING");
        }
        return null;
    }

    public List<Violation> isListCharacterItemValid(List<CharacterItemRequest> requests) {
        List<Violation> violations = new ArrayList<>();
        for (CharacterItemRequest request: requests) {
            Violation characteristicFound = isCharacteristicFound(request.getCharacteristic());
            if (characteristicFound == null) {
                Violation valueComplyType = isValueComplyType(request);
                if (valueComplyType != null) {
                    violations.add(valueComplyType);
                }
            } else {
                violations.add(characteristicFound);
            }
        }
        return violations;
    }

}

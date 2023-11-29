package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.enums.Type;
import com.vvnuts.shop.exceptions.StringAndNumValueTogetherException;
import com.vvnuts.shop.exceptions.ValueNotComplyTypeException;
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
        Optional<Characteristic> optional = characteristicRepository.findById(request.getCharacteristic());
        ValidationErrorResponse response = new ValidationErrorResponse();

        response.getViolations().add(isCharacteristicFound(optional, request.getCharacteristic()));
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

    private Violation isCharacteristicFound(Optional<Characteristic> optional, Integer characteristicId) {
        if (optional.isEmpty()) {
            return new Violation("Characteristic", "Характеристика с id "
                            + characteristicId + " not found");
        }
        return null;
    }

    private Violation isStringAndNumValueTogether(CharacterItemRequest request) {
        if (request.getNumValue() != null && request.getValue() != null) {
            return new Violation("CharacterItem",
                    "Численное и строковое значение у характеристики  c id " + request.getCharacteristic());
        }
        return null;
    }

    private void isValueComplyType(CharacterItemRequest request) {
        Characteristic characteristic = characteristicRepository.findById(request.getCharacteristic()).orElseThrow();
        if (characteristic.getType() == Type.STRING && request.getNumValue() != null) {
            throw new ValueNotComplyTypeException("Характеристика типа STRING не может иметь значениe INTEGER");
        }
        if (characteristic.getType() == Type.INTEGER && request.getValue() != null) {
            throw new ValueNotComplyTypeException("Характеристика типа STRING не может иметь значениe INTEGER");
        }
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

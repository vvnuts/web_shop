package com.vvnuts.shop.utils;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.responses.erorrs.StringAndNumValueTogetherException;
import com.vvnuts.shop.dtos.responses.erorrs.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.erorrs.Violation;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterItemUtils {
    private final CharacteristicRepository characteristicRepository;

    public List<CharacterItem> createListCharacterItems(ItemRequest itemRequest) {
        List<CharacterItem> characterItems = new ArrayList<>();
        ValidationErrorResponse response = new ValidationErrorResponse();

        for (CharacterItemRequest request : itemRequest.getCharacterItems()) {
            Violation violation = validate(request);
            if (violation != null) {
                response.getViolations().add(violation);
            }

            CharacterItem newCharacterItem = CharacterItem.builder()
                    .characteristic(characteristicRepository.findById(request.getCharacteristic())
                            .orElseThrow())
                    .numValue(request.getNumValue())
                    .value(request.getValue())
                    .build();
            characterItems.add(newCharacterItem);
        }
        if (response.getViolations().size() > 0) {
            throw new StringAndNumValueTogetherException(response);
        }
        return characterItems;
    }

    public CharacterItem transferRequestToEntity(CharacterItemRequest request) {
        Violation violation = validate(request);
        if (violation != null) {
            ValidationErrorResponse response = new ValidationErrorResponse();
            response.getViolations().add(violation);
            throw new StringAndNumValueTogetherException(response);
        }
        return CharacterItem.builder()
                .characteristic(characteristicRepository.findById(request.getCharacteristic())
                        .orElseThrow())
                .numValue(request.getNumValue())
                .value(request.getValue())
                .build();
    }

    public Violation validate(CharacterItemRequest request) {
        if (request.getNumValue() != null && request.getValue() != null) {
            return new Violation("CharacterItem",
                    "Численное и строковое значение у характеристики  c id " +
                            + request.getCharacteristic());
        }
        return null;
    }
}

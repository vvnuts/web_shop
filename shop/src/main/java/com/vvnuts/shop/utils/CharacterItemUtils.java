package com.vvnuts.shop.utils;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.repositories.CharacterItemRepository;
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
        for (CharacterItemRequest characterItemRequest : itemRequest.getCharacterItems()) {
            CharacterItem newCharacterItem = transferRequestToEntity(characterItemRequest);
            characterItems.add(newCharacterItem);
        }
        return characterItems;
    }

    public CharacterItem transferRequestToEntity(CharacterItemRequest request) {
        validate(request);
        return CharacterItem.builder()
                .characteristic(characteristicRepository.findById(request.getCharacteristic())
                        .orElseThrow())
                .numValue(request.getNumValue())
                .value(request.getValue())
                .build();
    }

    public void validate(CharacterItemRequest request) {
        if (request.getNumValue() != null && request.getValue() != null) {
            throw new RuntimeException("Одновременно численное и буквенное значение"); //TODO throw
        }
    }
}

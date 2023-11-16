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
            CharacterItem newCharacterItem = CharacterItem.builder()
                    .characteristic(characteristicRepository.findById(characterItemRequest.getCharacteristic())
                            .orElseThrow())
                    .numValue(characterItemRequest.getNumValue())
                    .value(characterItemRequest.getValue())
                    .build();
            characterItems.add(newCharacterItem);
        }
        return characterItems;
    }
}

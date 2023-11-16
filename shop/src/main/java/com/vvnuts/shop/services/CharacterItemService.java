package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterItemService {
    private final CharacterItemRepository characterItemRepository;

    public void addCategoryItemsCharacteristic(Category category, Characteristic characteristic) {
        for (Item item: category.getItems()) {
             CharacterItem characterItem = CharacterItem.builder()
                     .item(item)
                     .characteristic(characteristic)
                     .build();
             characterItemRepository.save(characterItem);
        }
    }

    public void removeCategoryItemsCharacteristic(Category category, Characteristic characteristic) {
        for (Item item: category.getItems()) {
            CharacterItem removeCharacterItem = characterItemRepository.findByCharacteristicAndItem(characteristic, item)
                    .orElseThrow();
            characterItemRepository.delete(removeCharacterItem);
        }
    }
}

package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.CharacterItemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterItemService {
    private final CharacterItemRepository characterItemRepository;
    private final CharacterItemUtils characterItemUtils;
    private final ItemRepository itemRepository;
    private final CharacteristicRepository characteristicRepository;

    public void create(Integer itemId, CharacterItemRequest request) {
        CharacterItem characterItem = characterItemUtils.transferRequestToEntity(request);
        Item item = itemRepository.findById(itemId).orElseThrow();
        characterItem.setItem(item);
        characterItemRepository.save(characterItem);
        item.getCharacterItems().add(characterItem);
        itemRepository.save(item);
        Characteristic characteristic = characterItem.getCharacteristic();
        characteristic.getCharacterItems().add(characterItem);
        characteristicRepository.save(characteristic);
    }

    public List<CharacterItem> findAllCharacterItemFromItem(Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        return item.getCharacterItems();
    }

    public void deleteCharacterItem(Integer characterItemId) {
        CharacterItem characterItem = characterItemRepository.findById(characterItemId).orElseThrow();
        characterItemRepository.delete(characterItem);
    }

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

package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.mappers.CharacterItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterItemService {
    private final CharacterItemRepository repository;
    private final CharacterItemMapper mapper;
    private final ItemRepository itemRepository;
    private final CharacteristicRepository characteristicRepository;

    public CharacterItem create(Integer itemId, CharacterItemRequest request) {
        CharacterItem characterItem = mapper.transferRequestToEntity(request);

        Item item = itemRepository.findById(itemId).orElseThrow();
        characterItem.setItem(item);
        characterItem = repository.save(characterItem);
        item.getCharacterItems().add(characterItem);
        itemRepository.save(item);

        Characteristic characteristic = characterItem.getCharacteristic();
        characteristic.getCharacterItems().add(characterItem);
        characteristicRepository.save(characteristic);
        return characterItem;
    }

    public CharacterItem findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public List<CharacterItem> findAllCharacterItemFromItem(Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        return item.getCharacterItems();
    }

    public CharacterItem update(CharacterItem updateCharacterItem, CharacterItemRequest request) {
        CharacterItem updateDTO = mapper.transferRequestToEntity(request);

        updateCharacterItem.setValue(updateDTO.getValue());
        updateCharacterItem.setNumValue(updateDTO.getNumValue());
        updateCharacterItem.setCharacteristic(updateDTO.getCharacteristic());

        return repository.save(updateCharacterItem);
    }

    public void deleteCharacterItem(Integer id) {
        CharacterItem characterItem = findById(id);
        repository.delete(characterItem);
    }

    public void addCategoryItemsCharacteristic(Category category, Characteristic characteristic) {
        for (Item item: category.getItems()) {
             CharacterItem characterItem = CharacterItem.builder()
                     .item(item)
                     .characteristic(characteristic)
                     .build();
             repository.save(characterItem);
        }
    }

    public void removeCategoryItemsCharacteristic(Category category, Characteristic characteristic) {
        for (Item item: category.getItems()) {
            Optional<CharacterItem> optional = repository.findByCharacteristicAndItem(characteristic, item);
            if (optional.isEmpty()) {
                continue;
            }
            CharacterItem removeCharacterItem = optional.get();
            repository.delete(removeCharacterItem);
        }
    }
}

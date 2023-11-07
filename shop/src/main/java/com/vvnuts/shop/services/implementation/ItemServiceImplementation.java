package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.ItemDTO;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.services.interfaces.CharacterItemService;
import com.vvnuts.shop.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation extends AbstractCrudService<Item, Integer> implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final CharacteristicRepository characteristicRepository;
    private final CharacterItemService characterItemService;
    @Override
    JpaRepository<Item, Integer> getRepository() {
        return itemRepository;
    }

    @Override
    public void create(Item entity) {
        for (CharacterItem characterItem: entity.getCharacterItems()) {
            characterItem.setItem(entity);
            characterItemService.create(characterItem);
        }
        super.create(entity);
    }

    @Override
    public void update(Item updateItem, Item updateDTO) {
        if (!updateItem.getItemName().equals(updateDTO.getItemName())) {
            updateItem.setItemName(updateDTO.getItemName());
        }
        int minSize = updateDTO.getCharacterItems().size();
        for (int i = 0; i < minSize; i++) {
            updateItem.getCharacterItems().get(i).setValue(updateDTO.getCharacterItems().get(i).getValue());
            updateItem.getCharacterItems().get(i).setNumValue(updateDTO.getCharacterItems().get(i).getNumValue());
        }
        if (!updateItem.getCategory().equals(updateDTO.getCategory())) {
            updateItem.getCategory().getItems().remove(updateItem);
            categoryRepository.save(updateItem.getCategory());
            updateItem.setCategory(updateDTO.getCategory());
            updateDTO.getCategory().getItems().add(updateItem);
            categoryRepository.save(updateDTO.getCategory());
        }
        if (!updateItem.getDescription().equals(updateDTO.getDescription())) {
            updateItem.setDescription(updateDTO.getDescription());
        }
        if (updateItem.getPrice() != updateDTO.getPrice()) {
            updateItem.setPrice(updateDTO.getPrice());
        }
        if (updateItem.getQuantity() != updateDTO.getQuantity()) {
            updateItem.setQuantity(updateDTO.getQuantity());
        }
        if (updateItem.getSale() != updateDTO.getSale()) {
            updateItem.setSale(updateDTO.getSale());
        }
        itemRepository.save(updateItem);
    }

    @Override
    public Item transferItemDtoToItem(ItemDTO itemDTO) {
        Item item = Item.builder()
                .category(categoryRepository.findByCategoryName(itemDTO.getCategory().getCategoryName()).orElseThrow())
                .itemName(itemDTO.getItemName())
                .description(itemDTO.getDescription())
                .price(itemDTO.getPrice())
                .quantity(itemDTO.getQuantity())
                .sale(itemDTO.getSale())
                .build();
        item.setCharacterItems(createListCharacterItems(itemDTO));
        return item;
    }

    public List<CharacterItem> createListCharacterItems(ItemDTO itemDTO) {
        List<CharacterItem> characterItems = new ArrayList<>();
        for (CharacterItem characterItemDto: itemDTO.getCharacterItems()) {
            CharacterItem newCharacterItem = CharacterItem.builder()
                    .characteristic(characteristicRepository.findByName(characterItemDto.getCharacteristic().getName())
                            .orElseThrow())
                    .numValue(characterItemDto.getNumValue())
                    .value(characterItemDto.getValue())
                    .build();
            characterItems.add(newCharacterItem);
        }
        return characterItems;
    }
}

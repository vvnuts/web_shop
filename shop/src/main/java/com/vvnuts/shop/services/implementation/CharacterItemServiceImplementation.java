package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.CharacterItemDTO;
import com.vvnuts.shop.dtos.CharacteristicDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.services.interfaces.CharacterItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterItemServiceImplementation extends AbstractCrudService<CharacterItem, CharacterItemDTO, Integer> implements CharacterItemService {
    private final CharacterItemRepository characterItemRepository;
    @Override
    JpaRepository<CharacterItem, Integer> getRepository() {
        return characterItemRepository;
    }

    @Override
    CharacterItem transferToUpdateEntity(CharacterItemDTO dto, CharacterItem updateEntity) {
        return null;
    }

    @Override
    CharacterItem transferToCreateEntity(CharacterItemDTO dto) {
        return null;
    }

    @Override
    public void addCategoryItemsCharacteristic(Category category, Characteristic characteristic) {
        for (Item item: category.getItems()) {
             CharacterItem characterItem = CharacterItem.builder()
                     .item(item)
                     .characteristic(characteristic)
                     .build();
             characterItemRepository.save(characterItem);
        }
    }

    @Override
    public void removeCategoryItemsCharacteristic(Category category, Characteristic characteristic) {
        for (Item item: category.getItems()) {
            CharacterItem removeCharacterItem = characterItemRepository.findByCharacteristicAndItem(characteristic, item)
                    .orElseThrow();
            characterItemRepository.delete(removeCharacterItem);
        }
    }
}

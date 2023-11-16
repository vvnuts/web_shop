package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.requests.CreateItemRequest;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation extends AbstractCrudService<Item, CreateItemRequest, Integer> implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final CharacteristicRepository characteristicRepository;
    private final CharacterItemRepository characterItemRepository;
    @Override
    JpaRepository<Item, Integer> getRepository() {
        return itemRepository;
    }

    @Override
    Item transferToUpdateEntity(CreateItemRequest dto, Item updateEntity) {
        Item updateDTO = transferItemDtoToItem(dto);
        if (!updateEntity.getItemName().equals(updateDTO.getItemName())) {
            updateEntity.setItemName(updateDTO.getItemName());
        }
        int minSize = updateDTO.getCharacterItems().size();
        for (int i = 0; i < minSize; i++) {
            updateEntity.getCharacterItems().get(i).setValue(updateDTO.getCharacterItems().get(i).getValue());
            updateEntity.getCharacterItems().get(i).setNumValue(updateDTO.getCharacterItems().get(i).getNumValue());
        }
        if (!updateEntity.getCategory().equals(updateDTO.getCategory())) {
            updateEntity.getCategory().getItems().remove(updateEntity);
            categoryRepository.save(updateEntity.getCategory());
            updateEntity.setCategory(updateDTO.getCategory());
            updateDTO.getCategory().getItems().add(updateEntity);
            categoryRepository.save(updateDTO.getCategory());
        }
        if (!updateEntity.getDescription().equals(updateDTO.getDescription())) {
            updateEntity.setDescription(updateDTO.getDescription());
        }
        if (updateEntity.getPrice() != updateDTO.getPrice()) {
            updateEntity.setPrice(updateDTO.getPrice());
        }
        if (updateEntity.getQuantity() != updateDTO.getQuantity()) {
            updateEntity.setQuantity(updateDTO.getQuantity());
        }
        if (updateEntity.getSale() != updateDTO.getSale()) {
            updateEntity.setSale(updateDTO.getSale());
        }
        return null;
    }

    @Override
    Item transferToCreateEntity(CreateItemRequest dto) {
        Item newItem = transferItemDtoToItem(dto);
        for (CharacterItem characterItem: newItem.getCharacterItems()) {
            characterItem.setItem(newItem);
            characterItemRepository.save(characterItem);
        }
        return null;
    }

    @Override
    public Item transferItemDtoToItem(CreateItemRequest createItemRequest) {
        Item item = Item.builder()
                .category(categoryRepository.findByCategoryName(createItemRequest.getCategory().getCategoryName()).orElseThrow())
                .itemName(createItemRequest.getItemName())
                .description(createItemRequest.getDescription())
                .price(createItemRequest.getPrice())
                .quantity(createItemRequest.getQuantity())
                .sale(createItemRequest.getSale())
                .build();
        item.setCharacterItems(createListCharacterItems(createItemRequest));
        return item;
    }

    public void calculateRatingItem (Item item) {
        float sumMark = 0;
        int countMark = 0;
        for (Review review: item.getReviews()) {
            sumMark += review.getMark();
            countMark += 1;
        }
        item.setMark(sumMark/countMark);
        itemRepository.save(item);
    }

    public List<CharacterItem> createListCharacterItems(CreateItemRequest createItemRequest) {
        List<CharacterItem> characterItems = new ArrayList<>();
        for (CharacterItemRequest characterItemRequest : createItemRequest.getCharacterItems()) {
            CharacterItem newCharacterItem = CharacterItem.builder()
                    .characteristic(characteristicRepository.findByName(characterItemRequest.getCharacteristic().getName())
                            .orElseThrow())
                    .numValue(characterItemRequest.getNumValue())
                    .value(characterItemRequest.getValue())
                    .build();
            characterItems.add(newCharacterItem);
        }
        return characterItems;
    }

    public ItemResponse convertEntityToResponse(Item item) {
        return  modelMapper.map(item, ItemResponse.class);
    }
}

package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.requests.ItemLowInfoRequest;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.CharacterItemUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final CharacterItemUtils characterItemUtils;
    private final CharacterItemRepository characterItemRepository;

    public void create(ItemRequest request) {
        Item newItem = transferItemDtoToItem(request);
        for (CharacterItem characterItem: newItem.getCharacterItems()) {
            characterItem.setItem(newItem);
            characterItemRepository.save(characterItem);
        }
        itemRepository.save(newItem);
    }

    public Item findById(Integer id) { //TODO переписать с респонсом
        return itemRepository.findById(id).orElse(null);
    }


    public List<Item> findAll() {
        return new ArrayList<>(itemRepository.findAll());
    }

    public void update(ItemRequest request, Integer id) {
        Item updateItem = findById(id);
        Item updateDTO = transferItemDtoToItem(request);
        if (!updateItem.getItemName().equals(updateDTO.getItemName())) {
            updateItem.setItemName(updateDTO.getItemName());
        }
        int minSize = updateDTO.getCharacterItems().size();
        for (int i = 0; i < minSize; i++) { //TODO переписать
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
        if (updateItem.getPrice().equals(updateDTO.getPrice())) {
            updateItem.setPrice(updateDTO.getPrice());
        }
        if (updateItem.getQuantity().equals(updateDTO.getQuantity())) {
            updateItem.setQuantity(updateDTO.getQuantity());
        }
        if (updateItem.getSale().equals(updateDTO.getSale())) {
            updateItem.setSale(updateDTO.getSale());
        }
        itemRepository.save(updateItem);
    }

    public void delete(Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        itemRepository.delete(item);
    }

    public Item transferItemDtoToItem(ItemRequest itemRequest) {
        Item item = Item.builder()
                .category(categoryRepository.findByCategoryName(itemRequest.getCategory().getCategoryName()).orElseThrow())
                .itemName(itemRequest.getItemName())
                .description(itemRequest.getDescription())
                .price(itemRequest.getPrice())
                .quantity(itemRequest.getQuantity())
                .sale(itemRequest.getSale())
                .build();
        item.setCharacterItems(characterItemUtils.createListCharacterItems(itemRequest));
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

    public ItemResponse convertEntityToResponse(Item item) {
        return  modelMapper.map(item, ItemResponse.class);
    }
}

package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final CategoryRepository categoryRepository;
    private final CharacterItemMapper characterItemMapper;
    private final ModelMapper modelMapper;

    public Item transferItemDtoToItem(ItemRequest itemRequest) {
        Item item = Item.builder()
                .category(categoryRepository.findById(itemRequest.getCategoryId()).orElseThrow())
                .itemName(itemRequest.getItemName())
                .description(itemRequest.getDescription())
                .price(itemRequest.getPrice())
                .quantity(itemRequest.getQuantity())
                .sale(itemRequest.getSale())
                .build();
        item.setCharacterItems(characterItemMapper.createListCharacterItems(itemRequest));
        return item;
    }

    public ItemResponse convertEntityToResponse(Item item) {
        return  modelMapper.map(item, ItemResponse.class);
    }
}

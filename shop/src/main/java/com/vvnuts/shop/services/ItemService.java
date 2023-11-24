package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.CharacterItemUtils;
import com.vvnuts.shop.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        }
        itemRepository.save(newItem);
    }

    public Item findById(Integer id) { //TODO переписать с респонсом
        return itemRepository.findById(id).orElseThrow();
    }

    public Set<Item> findItemFromCategory(Category startCategory) { //TODO Поменять на ItemResponse
        Set<Item> allItem = new HashSet<>(startCategory.getItems());
        List<Category> childrenCategory = startCategory.getChildren();
        for (Category child : childrenCategory) {
            allItem.addAll(findItemFromCategory(child));
            allItem.addAll(child.getItems());
        }
        return allItem;
    }

    public void update(ItemRequest request, Integer id) { //TODO сделать чтобы characterItem соответствовало категории
        Item updateItem = findById(id);
        Item updateDTO = transferItemDtoToItem(request);
        if (!updateItem.getItemName().equals(updateDTO.getItemName())) {
            updateItem.setItemName(updateDTO.getItemName());
        }
        List<CharacterItem> oldCharacterItem = updateItem.getCharacterItems();
        for (CharacterItem characterItem: oldCharacterItem) {
            characterItem.setItem(null);
            characterItemRepository.save(characterItem);
        }
        updateItem.setCharacterItems(updateDTO.getCharacterItems());
        for (CharacterItem characterItem: updateItem.getCharacterItems()) {
            characterItem.setItem(updateItem);
//            characterItemRepository.save(characterItem);
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
        if (!updateItem.getPrice().equals(updateDTO.getPrice())) {
            updateItem.setPrice(updateDTO.getPrice());
        }
        if (!updateItem.getQuantity().equals(updateDTO.getQuantity())) {
            updateItem.setQuantity(updateDTO.getQuantity());
        }
        if (!updateItem.getSale().equals(updateDTO.getSale())) {
            updateItem.setSale(updateDTO.getSale());
        }
        itemRepository.save(updateItem);
    }

    public void uploadImage(MultipartFile file, Integer itemId) throws IOException {
        if (file.isEmpty()){
            return; //TODO throw
        }
        Item item = findById(itemId);
        item.setImage(ImageUtils.compressImage(file.getBytes()));
        itemRepository.save(item);
    }

    @Transactional
    public byte[] downloadImage(Integer itemId) {
        Item item = findById(itemId);
        return ImageUtils.decompressImage(item.getImage());
    }

    public void delete(Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        itemRepository.delete(item);
    }

    public Item transferItemDtoToItem(ItemRequest itemRequest) {
        Item item = Item.builder()
                .category(categoryRepository.findById(itemRequest.getCategoryId()).orElseThrow())
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

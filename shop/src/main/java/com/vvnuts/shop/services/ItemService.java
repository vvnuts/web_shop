package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.exceptions.ImageIsAlreadyNull;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.ImageUtils;
import com.vvnuts.shop.utils.mappers.ItemMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;
    private final CategoryRepository categoryRepository;
    private final CharacterItemRepository characterItemRepository;

    public Item create(ItemRequest request) {
        Item newItem = mapper.transferItemDtoToItem(request);
        for (CharacterItem characterItem: newItem.getCharacterItems()) {
            characterItem.setItem(newItem);
        }
        return repository.save(newItem);
    }

    public Item findById(Integer id) { //TODO переписать с респонсом
        return repository.findById(id).orElseThrow();
    }

    public Item update(ItemRequest request, Item updateItem) {
        Item updateDTO = mapper.transferItemDtoToItem(request);
        if (!updateItem.getItemName().equals(updateDTO.getItemName())) {
            updateItem.setItemName(updateDTO.getItemName());
        }

        for (CharacterItem characterItem: updateItem.getCharacterItems()) {
            characterItem.setItem(null);
            characterItemRepository.save(characterItem);
        }
        updateItem.setCharacterItems(updateDTO.getCharacterItems());
        for (CharacterItem characterItem: updateItem.getCharacterItems()) {
            characterItem.setItem(updateItem);
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
        return repository.save(updateItem);
    }

    public Item uploadImage(MultipartFile file, Integer itemId) throws IOException {
        if (file.isEmpty()){
            throw new FileIsEmptyException("Файл пуст.");
        }
        Item item = findById(itemId);
        item.setImage(ImageUtils.compressImage(file.getBytes()));
        return repository.save(item);
    }

    @Transactional
    public byte[] downloadImage(Integer itemId) {
        Item item = findById(itemId);
        if (item.getImage() == null) {
            return null;
        }
        return ImageUtils.decompressImage(item.getImage());
    }

    public Item deleteImage(Integer imageId) {
        Item item = findById(imageId);
        if (item.getImage() == null) {
            throw new ImageIsAlreadyNull("Изображение и так уже пустое!");
        }
        item.setImage(null);
        return repository.save(item);
    }

    public void delete(Integer itemId) {
        Item item = repository.findById(itemId).orElseThrow();
        repository.delete(item);
    }

    public Item calculateRatingItem (Item item) {
        double sumMark = 0.;
        int countMark = 0;
        for (Review review: item.getReviews()) {
            sumMark += review.getMark();
            countMark += 1;
        }
        item.setMark(Math.round(sumMark/countMark * 100)/100.);
        return repository.save(item);
    }
}

package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.utils.mappers.CharacteristicMapper;
import com.vvnuts.shop.utils.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CharacterItemService characterItemService;
    private final CharacteristicMapper mapper;

    public void create(CharacteristicRequest request) {
        Characteristic characteristic = Characteristic.builder()
                .name(request.getName())
                .categories(categoryMapper.getCategoryListFromIds(request.getCategories()))
                .type(request.getType())
                .build();
        characteristicRepository.save(characteristic);
        for (Category category: characteristic.getCategories()) {
            category.getCharacteristics().add(characteristic);
            characterItemService.addCategoryItemsCharacteristic(category, characteristic);
            categoryRepository.save(category);
        }
    }

    public Characteristic findById(Integer id) {
        return characteristicRepository.findById(id).orElseThrow();
    }

    public CharacteristicResponse findOne(Integer id) {
        Characteristic characteristic = characteristicRepository.findById(id).orElseThrow();
        return mapper.convertEntityToResponse(characteristic);
    }

    public List<CharacteristicResponse> findAll() {
        List<CharacteristicResponse> characteristicResponses = new ArrayList<>();
        for (Characteristic characteristic: characteristicRepository.findAll()) {
            characteristicResponses.add(mapper.convertEntityToResponse(characteristic));
        }
        return characteristicResponses;
    }

    public void update(CharacteristicRequest request, Characteristic updateCharacteristic) {
        Characteristic updateDto = Characteristic.builder()
                .name(request.getName())
                .categories(categoryMapper.getCategoryListFromIds(request.getCategories()))
                .type(request.getType())
                .build();
        updateCharacteristic.setName(request.getName());

        Set<Category> oldCategories = new HashSet<>(updateCharacteristic.getCategories());
        for (Category newCategory : updateDto.getCategories()) {
            if (!oldCategories.contains(newCategory)) {
                updateCharacteristic.getCategories().add(newCategory);
                newCategory.getCharacteristics().add(updateCharacteristic);
                characterItemService.addCategoryItemsCharacteristic(newCategory, updateCharacteristic);
                categoryRepository.save(newCategory);
            } else {
                oldCategories.remove(newCategory);
            }
        }
        if (oldCategories.size() > 0) {
            for (Category removeCategory : oldCategories) {
                updateCharacteristic.getCategories().remove(removeCategory);
                removeCategory.getCharacteristics().remove(updateCharacteristic);
                characterItemService.removeCategoryItemsCharacteristic(removeCategory, updateCharacteristic);
                categoryRepository.save(removeCategory);
            }
        }

        characteristicRepository.save(updateCharacteristic);
    }

    public void delete(Integer characteristicId) {
        Characteristic characteristic = characteristicRepository.findById(characteristicId).orElseThrow();
        for (Category category: characteristic.getCategories()) {
            category.getCharacteristics().remove(characteristic);
            categoryRepository.save(category);
        }
        characteristicRepository.delete(characteristic);
    }

    public void replaceCharacteristicsInCategory (Category updateCategory, Category updateDTO) {
        Set<Characteristic> oldCharacteristic = new HashSet<>(updateCategory.getCharacteristics());
        for (Characteristic newCharacteristic : updateDTO.getCharacteristics()) {
            if (!oldCharacteristic.contains(newCharacteristic)) {
                updateCategory.getCharacteristics().add(newCharacteristic);
                newCharacteristic.getCategories().add(updateCategory);
                characterItemService.addCategoryItemsCharacteristic(updateCategory, newCharacteristic);
                characteristicRepository.save(newCharacteristic);
            } else {
                oldCharacteristic.remove(newCharacteristic);
            }
        }
        if (oldCharacteristic.size() > 0) {
            for (Characteristic removeCharacteristic : oldCharacteristic) {
                updateCategory.getCharacteristics().remove(removeCharacteristic);
                removeCharacteristic.getCategories().remove(updateCategory);
                characterItemService.removeCategoryItemsCharacteristic(updateCategory, removeCharacteristic);
                characteristicRepository.save(removeCharacteristic);
            }
        }
    }
}

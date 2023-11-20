package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.utils.CategoryUtils;
import com.vvnuts.shop.utils.CharacteristicUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CharacteristicService characteristicService;
    private final ItemService itemService;
    private final CharacteristicUtils characteristicUtils;
    private final CategoryUtils categoryUtils;
    private final ModelMapper modelMapper;

    public void create(CategoryRequest categoryRequest) {
        Category newCategory = transferCategoryDtoToCategory(categoryRequest);
        for (Category parent: newCategory.getParents()) {
            parent.getChildren().add(newCategory);
        }
        for (Characteristic characteristic: newCategory.getCharacteristics()) {
            characteristic.getCategories().add(newCategory);
        }
        categoryRepository.save(newCategory);
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Set<Item> findItemInCategory(Integer id) {
        return itemService.findItemFromCategory(findById(id));
    }

    public List<CategoryResponse> findAll() {
        return convertEntityToListResponse(categoryRepository.findAll());
    }

    public void update(CategoryRequest request, Integer id) {
        Category updateCategory = findById(id);
        Category updateDTO = transferCategoryDtoToCategory(request);
        if (!updateCategory.getCategoryName().equals(updateDTO.getCategoryName())) {
            updateCategory.setCategoryName(updateDTO.getCategoryName());
        }

        for (Category oldParent : updateCategory.getParents()) {
            oldParent.getChildren().remove(updateCategory);
            categoryRepository.save(oldParent);
        }
        for (Category updateParent : updateDTO.getParents()) {
            updateParent.getChildren().add(updateCategory);
            categoryRepository.save(updateParent);
        }

        characteristicService.replaceCharacteristicsInCategory(updateCategory, updateDTO);
        categoryRepository.save(updateCategory);
    }

    public void delete(Integer categoryId) {
        Category category = findById(categoryId);
        for (Category parent: category.getParents()) {
            parent.getChildren().remove(category);
            categoryRepository.save(parent);
        }
        List<Category> children = category.getChildren();
        for (Category child: children) {
            if (child.getParents().size() > 1) {
                child.getParents().remove(category);
                category.getChildren().remove(child);
                categoryRepository.save(child);
            }
        }
        categoryRepository.delete(category);
    }

    public Category transferCategoryDtoToCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .parents(categoryUtils.getCategoryListFromIds(categoryRequest.getParents()))
                .characteristics(characteristicUtils.getCharacteristicListFromDTO(categoryRequest.getCharacteristics()))
                .build();
    }

    public List<CategoryResponse> convertEntityToListResponse(List<Category> categories) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category: categories) {
            categoryResponses.add(convertEntityToResponse(category));
        }
        return categoryResponses;
    }

    public CategoryResponse convertEntityToResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }
}

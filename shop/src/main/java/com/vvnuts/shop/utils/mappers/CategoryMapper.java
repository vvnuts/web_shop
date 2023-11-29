package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryMapper {
    private final CategoryRepository categoryRepository;
    private final CharacteristicMapper characteristicMapper;
    private final ModelMapper modelMapper;

    public List<Category> getCategoryListFromIds(Collection<Integer> categoriesId) {
        List<Category> categories = new ArrayList<>();
        ValidationErrorResponse response = new ValidationErrorResponse();
        for (Integer categoryId : categoriesId) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            categories.add(category);
        }
        Collections.sort(categories);
        return categories;
    }

    public Category transferCategoryDtoToCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .parents(getCategoryListFromIds(categoryRequest.getParents()))
                .characteristics(characteristicMapper.getCharacteristicListFromDTO(categoryRequest.getCharacteristics()))
                .build();
    }

    public List<CategoryResponse> convertEntityToListResponse(Collection<Category> categories) {
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

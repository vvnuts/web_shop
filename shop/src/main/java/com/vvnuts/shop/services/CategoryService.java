package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.entities.*;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.utils.mappers.CategoryMapper;
import com.vvnuts.shop.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CharacteristicService characteristicService;
    private final ItemService itemService;
    private final CategoryMapper categoryMapper;

    public void create(CategoryRequest categoryRequest) {
        Category newCategory = categoryMapper.transferCategoryDtoToCategory(categoryRequest);
        for (Category parent: newCategory.getParents()) {
            parent.getChildren().add(newCategory);
        }
        for (Characteristic characteristic: newCategory.getCharacteristics()) {
            characteristic.getCategories().add(newCategory);
        }
        categoryRepository.save(newCategory);
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Set<Item> findItemInCategory(Integer id) {
        return itemService.findItemFromCategory(findById(id));
    }

    public List<CategoryResponse> findAll() {
        return categoryMapper.convertEntityToListResponse(categoryRepository.findAll());
    }

    public void update(CategoryRequest request, Category updateCategory) {
        Category updateDTO = categoryMapper.transferCategoryDtoToCategory(request);
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
        for (Category parent : category.getParents()) {
            parent.getChildren().remove(category);
            categoryRepository.save(parent);
        }
        deleteChildren(category.getChildren(), category);
        categoryRepository.delete(category);
    }

    public void deleteChildren(List<Category> categories, Category removeCategory) {
        for (Category child: categories) {
            if (child.getChildren() != null) {
                deleteChildren(child.getChildren(), removeCategory);
            }
            if (child.getParents().size() > 1) {
                child.getParents().remove(removeCategory);
                removeCategory.getChildren().remove(child);
                categoryRepository.save(child);
            }
        }
    }

    public void uploadImage(MultipartFile file, Integer categoryId) throws IOException {
        if (file.isEmpty()){
            throw new FileIsEmptyException("Файл пуст.");
        }
        Category category = findById(categoryId);
        category.setImage(ImageUtils.compressImage(file.getBytes()));
        categoryRepository.save(category);
    }

    @Transactional
    public byte[] downloadImage(Integer categoryId) {
        Category category = findById(categoryId);
        return ImageUtils.decompressImage(category.getImage());
    }

    public void deleteImage(Integer imageId) {
        Category category = findById(imageId);
        categoryRepository.delete(category);
    }
}

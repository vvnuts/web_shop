package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.entities.*;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.exceptions.ImageIsAlreadyNull;
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
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final CharacteristicService characteristicService;

    public Category create(CategoryRequest categoryRequest) {
        Category newCategory = mapper.transferCategoryDtoToCategory(categoryRequest);
        for (Category parent: newCategory.getParents()) {
            parent.getChildren().add(newCategory);
        }
        for (Characteristic characteristic: newCategory.getCharacteristics()) {
            characteristic.getCategories().add(newCategory);
        }
        return repository.save(newCategory);
    }

    public Category findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public List<CategoryResponse> findAll() {
        return mapper.convertEntityToListResponse(repository.findAll());
    }

    public Category update(CategoryRequest request, Category updateCategory) {
        Category updateDTO = mapper.transferCategoryDtoToCategory(request);
        if (!updateCategory.getCategoryName().equals(updateDTO.getCategoryName())) {
            updateCategory.setCategoryName(updateDTO.getCategoryName());
        }

        for (Category oldParent : updateCategory.getParents()) {
            oldParent.getChildren().remove(updateCategory);
            repository.save(oldParent);
        }
        for (Category updateParent : updateDTO.getParents()) {
            updateParent.getChildren().add(updateCategory);
            repository.save(updateParent);
        }

        characteristicService.replaceCharacteristicsInCategory(updateCategory, updateDTO);
        return repository.save(updateCategory);
    }

    public void delete(Integer categoryId) { //TODO подумать над удалением из списка удаляемых если несколько родителей
        Category category = findById(categoryId);
        for (Category parent : category.getParents()) {
            parent.getChildren().remove(category);
            repository.save(parent);
        }
        deleteChildren(category.getChildren(), category);
        repository.delete(category);
    }

    private void deleteChildren(List<Category> categories, Category removeCategory) {
        for (Category child: categories) {
            if (child.getChildren() != null) {
                deleteChildren(child.getChildren(), removeCategory);
            }
            if (child.getParents().size() > 1) {
                child.getParents().remove(removeCategory);
                removeCategory.getChildren().remove(child);
                repository.save(child);
            }
        }
    }

    public Category uploadImage(MultipartFile file, Integer categoryId) throws IOException {
        if (file.isEmpty()){
            throw new FileIsEmptyException("Файл пуст.");
        }
        Category category = findById(categoryId);
        category.setImage(ImageUtils.compressImage(file.getBytes()));
        return repository.save(category);
    }

    @Transactional
    public byte[] downloadImage(Integer categoryId) {
        Category category = findById(categoryId);
        if (category.getImage() == null) {
            return null;
        }
        return ImageUtils.decompressImage(category.getImage());
    }

    public Category deleteImage(Integer userId) {
        Category category = findById(userId);
        if (category.getImage() == null) {
            throw new ImageIsAlreadyNull("Изображение и так уже пустое!");
        }
        category.setImage(null);
        return repository.save(category);
    }
}

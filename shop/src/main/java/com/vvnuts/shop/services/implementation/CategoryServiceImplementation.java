package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.requests.CreateCategoryRequest;
import com.vvnuts.shop.dtos.requests.ParentRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.services.interfaces.CategoryService;
import com.vvnuts.shop.services.interfaces.CharacterItemService;
import com.vvnuts.shop.services.interfaces.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation extends AbstractCrudService<Category, CreateCategoryRequest, Integer> implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CharacterItemService characterItemService;
    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicService characteristicService;
    @Override
    JpaRepository<Category, Integer> getRepository() {
        return categoryRepository;
    }

    @Override
    Category transferToUpdateEntity(CreateCategoryRequest dto, Category updateCategory) {
        Category updateDTO = transferCategoryDtoToCategory(dto);
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

        Set<Characteristic> oldCharacteristic = new HashSet<>(updateCategory.getCharacteristics());
        for (Characteristic newCharacteristic : updateDTO.getCharacteristics()) {
            if (!oldCharacteristic.contains(newCharacteristic)) {
                updateCategory.getCharacteristics().add(newCharacteristic);
                if (newCharacteristic.getCategories() == null) {
                    List<Category> temp = new ArrayList<>();
                    temp.add(updateCategory);
                    newCharacteristic.setCategories(temp);
                } else {
                    newCharacteristic.getCategories().add(updateCategory);
                }
                characterItemService.addCategoryItemsCharacteristic(updateCategory, newCharacteristic); // TODO Проверить работает ли. Есть сомнение по отсутсвию id в newCharacteristic
                characteristicRepository.save(newCharacteristic);
            } else {
                oldCharacteristic.remove(newCharacteristic);
            }
        }
        if (oldCharacteristic.size() > 0) {
            for (Characteristic removeCharacteristic : oldCharacteristic) {
                updateCategory.getCharacteristics().remove(removeCharacteristic);
                removeCharacteristic.getCategories().remove(updateCategory);
                characterItemService.removeCategoryItemsCharacteristic(updateCategory, removeCharacteristic);  //TODO смущает Category
                characteristicRepository.save(removeCharacteristic);
            }
        }

        return updateCategory;
    }

    @Override
    Category transferToCreateEntity(CreateCategoryRequest dto) {
        Category newCategory = transferCategoryDtoToCategory(dto);
        for (Category parent: newCategory.getParents()) {
            parent.getChildren().add(newCategory);
        }
        for (Characteristic characteristic: newCategory.getCharacteristics()) {
            characteristic.getCategories().add(newCategory);
        }
        return newCategory;
    }

    @Override
    public List<Item> getItemsFromCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        return category.getItems();
    }

    @Override
    public void update(Category updateCategory, Category updateDTO) {

    }

    @Override
    public Category transferCategoryDtoToCategory(CreateCategoryRequest createCategoryRequest) {
        return Category.builder()
                .categoryName(createCategoryRequest.getCategoryName())
                .parents(getCategoryListFromDTO(createCategoryRequest.getParents()))
                .characteristics(characteristicService.getCharacteristicListFromDTO(createCategoryRequest.getCharacteristics()))
                .build();
    }

    public List<Category> getCategoryListFromDTO (List<ParentRequest> parentRequests) { //TODO проверка на уникальность категорий. В идеале - Set
        List<Category> categories = new ArrayList<>();
        for (ParentRequest parentRequest: parentRequests) {
            Category category = categoryRepository.findByCategoryName(parentRequest.getCategoryName()).orElseThrow();
            categories.add(category);
        }
        Collections.sort(categories);
        return categories;
    }
}

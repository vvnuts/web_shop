package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.CategoryDTO;
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
public class CategoryServiceImplementation extends AbstractCrudService<Category, Integer> implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CharacterItemService characterItemService;
    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicService characteristicService;
    @Override
    JpaRepository<Category, Integer> getRepository() {
        return categoryRepository;
    }

    @Override
    public void create(Category entity) {
        for (Category parent: entity.getParents()) {
            parent.getChildren().add(entity);
        }
        for (Characteristic characteristic: entity.getCharacteristics()) {
            characteristic.getCategories().add(entity);
        }
        super.create(entity);
    }

    @Override
    public List<Item> getItemsFromCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        return category.getItems();
    }

    @Override
    public void update(Category updateCategory, Category updateDTO) {
        if (!updateCategory.getCategoryName().equals(updateDTO.getCategoryName())) {
            updateCategory.setCategoryName(updateDTO.getCategoryName());
        }
        Collections.sort(updateDTO.getParents());
        boolean isParentsChange = false;
        if (updateCategory.getParents().size() != updateDTO.getParents().size()) {
            isParentsChange = true;
        } else {
            int minSize = updateDTO.getParents().size();
            for (int i = 0; i < minSize; i++) {
                if (updateCategory.getParents().get(i) != updateDTO.getParents().get(i)) {
                    isParentsChange = true;
                    break;
                }
            }
        }
        if (isParentsChange) {
            for (Category oldParent: updateCategory.getParents()) {
                oldParent.getChildren().remove(updateCategory);
                categoryRepository.save(oldParent);
            }
            for (Category updateParent: updateDTO.getParents()) {
                updateParent.getChildren().add(updateCategory);
                categoryRepository.save(updateParent);
            }
        }
        Set<Characteristic> oldCharacteristic = new HashSet<>(updateCategory.getCharacteristics());
        for (Characteristic newCharacteristic: updateDTO.getCharacteristics()) {
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
            for (Characteristic removeCharacteristic: oldCharacteristic) {
                updateCategory.getCharacteristics().remove(removeCharacteristic);
                removeCharacteristic.getCategories().remove(updateCategory);
                characterItemService.removeCategoryItemsCharacteristic(updateCategory, removeCharacteristic);  //TODO смущает Category
                characteristicRepository.save(removeCharacteristic);
            }
        }
        categoryRepository.save(updateCategory);
    }

    @Override
    public Category transferCategoryDtoToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .parents(getCategoryListFromDTO(categoryDTO.getParents()))
                .characteristics(characteristicService.getCharacteristicListFromDTO(categoryDTO.getCharacteristics()))
                .build();
    }

    public List<Category> getCategoryListFromDTO (List<Category> categoriesDTO) { //TODO проверка на уникальность категорий. В идеале - Set
        List<Category> categories = new ArrayList<>();
        for (Category categoryDTO: categoriesDTO) {
            Category category = categoryRepository.findByCategoryName(categoryDTO.getCategoryName()).orElseThrow();
            categories.add(category);
        }
        Collections.sort(categories);
        return categories;
    }
}

package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
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
    public void update(Category oldCategory, Category updateCategory) {
        Collections.sort(updateCategory.getParents());
        boolean isParentsChange = false;
        if (oldCategory.getParents().size() != updateCategory.getParents().size()) {
            isParentsChange = true;
        } else {
            int minSize = updateCategory.getParents().size();
            for (int i = 0; i < minSize; i++) {
                if (oldCategory.getParents().get(i) != updateCategory.getParents().get(i)) {
                    isParentsChange = true;
                    break;
                }
            }
        }
        if (isParentsChange) {
            for (Category oldParent: oldCategory.getParents()) {
                oldParent.getChildren().remove(oldCategory);
                categoryRepository.save(oldParent);
            }
            for (Category updateParent: updateCategory.getParents()) {
                updateParent.getChildren().add(updateCategory);
                categoryRepository.save(updateParent);
            }
        }
        Set<Characteristic> oldCharacteristic = new HashSet<>(oldCategory.getCharacteristics());
        for (Characteristic newCharacteristic: updateCategory.getCharacteristics()) {
            if (!oldCharacteristic.contains(newCharacteristic)) {
                newCharacteristic.getCategories().add(updateCategory);
                characterItemService.addCategoryItemsCharacteristic(oldCategory, newCharacteristic); // TODO Проверить работает ли. Есть сомнение по отсутсвию id в newCharacteristic
                characteristicRepository.save(newCharacteristic);
            }
            oldCharacteristic.remove(newCharacteristic);
        }
        if (oldCharacteristic.size() > 0) {
            for (Characteristic removeCharacteristic: oldCharacteristic) {
                removeCharacteristic.getCategories().remove(oldCategory);
                characterItemService.removeCategoryItemsCharacteristic(oldCategory, removeCharacteristic);  //TODO смущает Category
                characteristicRepository.save(removeCharacteristic);
            }
        }
        updateCategory.setCategoryId(oldCategory.getCategoryId());
        categoryRepository.save(updateCategory);
    }

    @Override
    public Category transferCategoryDtoToCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .categoryName(categoryDTO.getCategoryName())
                .parents(getCategoryListFromDTO(categoryDTO.getParents()))
                .characteristics(characteristicService.getCharacteristicListFromDTO(categoryDTO.getCharacteristics()))
                .build();
        return category;
    }

    public List<Category> getCategoryListFromDTO (List<Category> categoriesDTO) { //TODO проверка на уникальность категорий. В идеале - Set
        List<Category> categories = new ArrayList<>();
        for (Category categoryDTO: categoriesDTO) {
            Optional<Category> category = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
            if (category.isEmpty()){
                return null; //TODO throw
            }
            categories.add(category.get());
        }
        Collections.sort(categories);
        return categories;
    }
}

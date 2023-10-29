package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.services.interfaces.CategoryService;
import com.vvnuts.shop.services.interfaces.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation extends AbstractCrudService<Category, Integer> implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CharacteristicService characteristicService;
    @Override
    JpaRepository<Category, Integer> getRepository() {
        return categoryRepository;
    }

    @Override
    public void create(Category entity) {
        super.create(entity);
    }

    @Override
    public void update(CategoryDTO categoryDTO, Integer id) {
        Optional<Category> updateCategory = categoryRepository.findById(id);
        if (updateCategory.isEmpty()) {
            return; //TODO throw
        }
        Category updCategory = updateCategory.get();
        if (!updCategory.getCategoryName().equals(categoryDTO.getCategoryName())) {
            updCategory.setCategoryName(categoryDTO.getCategoryName());
        }

        List<Category> currentParents = updCategory.getParents();
        List<Integer> parentsId = new ArrayList<>();
        for (Category parent: updCategory.getParents()) {
            parentsId.add(parent.getCategoryId());
        }
        if (!parentsId.equals(categoryDTO.getParentsId())) {
            for (Category currentParent : currentParents) {
                currentParent.getChildren().remove(updCategory);
            }
            updCategory.setParents(transferIdToListCategory(categoryDTO.getParentsId(), updCategory));
        }

        updCategory.setCharacteristics(characteristicService.transferIdsToCharacteristicList(categoryDTO.getCharacteristicsId(), updCategory));  //TODO Поправить. Ужасно это
    }

    @Override
    public List<Category> transferIdToListCategory(List<Integer> ids, Category newCategory) {
        List<Category> categories = new ArrayList<>();
        Collections.sort(ids);
        for (Integer id: ids) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()){
                return null; //TODO throw
            }
            categories.add(category.get());
            category.get().getChildren().add(newCategory);
        }
        return categories;
    }
}

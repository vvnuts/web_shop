package com.vvnuts.shop.utils;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.erorrs.CycleHasFormedException;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryUtils {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategoryListFromIds(List<Integer> categoriesId) { //TODO проверка на уникальность категорий. В идеале - Set
        List<Category> categories = new ArrayList<>();
        for (Integer categoryId : categoriesId) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            categories.add(category);
        }
        Collections.sort(categories);
        return categories;
    }

    public void validate(CategoryRequest request, Integer id) throws CycleHasFormedException {
        if (request.getParents().contains(id)) {
            throw new RuntimeException("Pupus"); 
        }
        Category category = categoryRepository.findById(id).orElseThrow();
        List<Category> categories = getCategoryListFromIds(request.getParents());
        categories.add(category);
        if (hasCycle(category, categories)) {
            throw new CycleHasFormedException();
        };
    }

    public boolean hasCycle(Category category, List<Category> newParents) {
        for (Category parent: newParents) {
            if (isCategoryParentParents(category, parent)) {
                return true;
            }
        }

        return false;
    }

    public boolean isCategoryParentParents(Category category, Category parent){
        List<Category> allParents = parent.getParents();
        if (allParents.contains(category)) {
            return true;
        }
        for (Category grandParent: parent.getChildren()) {
            if (isCategoryParentParents(category, grandParent)) {
                return true;
            }
        }
        return false;
    }
}

package com.vvnuts.shop.utils;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.erorrs.*;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryUtils {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategoryListFromIds(List<Integer> categoriesId) {
        List<Category> categories = new ArrayList<>();
        ValidationErrorResponse response = new ValidationErrorResponse();
        for (Integer categoryId : categoriesId) {
            Optional<Category> optional = categoryRepository.findById(categoryId);
            if (optional.isPresent()) {
                categories.add(optional.get());
            } else {
                response.getViolations()
                        .add(new Violation("parents", "id = " + categoryId + " not found"));
            }
        }
        if (response.getViolations().size() > 0) {
            throw new NotFoundRelatedObjectException(response);
        }
        Collections.sort(categories);
        return categories;
    }

    public void validate(CategoryRequest request, Integer id) {
        if (request.getParents().contains(id)) {
            throw new CategoryParentContainsItselfException("В списке родителей категории присутствует сама категория");
        }
        Category category = categoryRepository.findById(id).orElseThrow();
        List<Category> categories = getCategoryListFromIds(request.getParents());
        categories.add(category);
        if (hasCycle(category, categories)) {
            throw new CycleHasFormedException("При текущих родителях образуется цикл");
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

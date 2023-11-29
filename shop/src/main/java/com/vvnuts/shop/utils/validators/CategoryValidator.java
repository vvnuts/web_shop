package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.exceptions.CategoryParentContainsItselfException;
import com.vvnuts.shop.exceptions.CycleHasFormedException;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.exceptions.NotFoundRelatedObjectException;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.utils.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository categoryRepository;
    private final CharacteristicRepository characteristicRepository;
    private final CategoryMapper categoryMapper;

    public void validate(CategoryRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.getViolations().addAll(isAllCategoryFound(request.getParents()));
        response.getViolations().addAll(isAllCharacteristicFound(request.getCharacteristics()));
        if (response.getViolations().size() > 0) {
            throw new NotFoundRelatedObjectException(response);
        }
    }

    public Category validate(CategoryRequest request, Integer id) {
        if (request.getParents().contains(id)) {
            throw new CategoryParentContainsItselfException("В списке родителей категории присутствует сама категория");
        }
        validate(request);

        Category category = categoryRepository.findById(id).orElseThrow();
        List<Category> categories = categoryMapper.getCategoryListFromIds(request.getParents());
        categories.add(category);
        if (hasCycle(category, categories)) {
            throw new CycleHasFormedException("При текущих родителях образуется цикл");
        }

        return category;
    }

    public boolean hasCycle(Category category, List<Category> newParents) {
        for (Category parent: newParents) {
            if (isCategoryParentParents(category, parent)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCategoryParentParents(Category category, Category parent) {
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

    public List<Violation> isAllCharacteristicFound(Collection<Integer> characteristics) {
        List<Violation> violations = new ArrayList<>();
        for (Integer characteristicId : characteristics) {
            Optional<Characteristic> optional = characteristicRepository.findById(characteristicId);
            if (optional.isEmpty()) {
                violations.add(new Violation("characteristic", "id = " + characteristicId + " not found"));
            }
        }
        return violations;
    }

    public List<Violation> isAllCategoryFound(Collection<Integer> parents) {
        List<Violation> violations = new ArrayList<>();
        for (Integer categoryId : parents) {
            Optional<Category> optional = categoryRepository.findById(categoryId);
            if (optional.isEmpty()) {
                violations.add(new Violation("parents", "id = " + categoryId + " not found"));
            }
        }
        return violations;
    }
}

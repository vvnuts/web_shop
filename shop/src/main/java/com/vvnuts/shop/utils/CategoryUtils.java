package com.vvnuts.shop.utils;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}

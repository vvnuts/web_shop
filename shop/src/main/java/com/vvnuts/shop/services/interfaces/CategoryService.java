package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;

import java.util.List;

public interface CategoryService extends CrudService<Category, Integer>{
    Category transferCategoryDtoToCategory(CategoryDTO categoryDTO);
    void update(Category oldCategory, Category updateCategory);
    List<Item> getItemsFromCategory(Integer categoryId);
}

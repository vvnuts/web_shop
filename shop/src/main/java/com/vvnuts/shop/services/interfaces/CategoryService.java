package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.CreateCategoryRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;

import java.util.List;

public interface CategoryService extends CrudService<Category, CreateCategoryRequest, Integer>{
    Category transferCategoryDtoToCategory(CreateCategoryRequest createCategoryRequest);
    void update(Category updateCategory, Category updateDTO);
    List<Item> getItemsFromCategory(Integer categoryId);
}

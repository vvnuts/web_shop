package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;

import java.util.List;

public interface CategoryService extends CrudService<Category, Integer>{
    Category transferCategoryDtoToCategory(CategoryDTO categoryDTO);
    List<Category> transferIdToListCategory(List<Integer> ids, Category newCategory);
}

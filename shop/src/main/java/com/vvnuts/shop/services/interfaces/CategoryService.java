package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;

import java.util.List;

public interface CategoryService extends CrudService<Category, Integer>{
    void update(CategoryDTO categoryDTO, Integer id);
    List<Category> transferIdToListCategory(List<Integer> ids, Category newCategory);
}

package com.vvnuts.shop.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.vvnuts.shop.dtos.requests.CreateCategoryRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.services.interfaces.CategoryService;
import com.vvnuts.shop.services.interfaces.CrudService;
import com.vvnuts.shop.utils.Views;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CategoryController extends AbstractCrudController<Category, CreateCategoryRequest, Integer>{
    private final CategoryService categoryService;

    @Override
    CrudService<Category, CreateCategoryRequest, Integer> getService() {
        return categoryService;
    }

    @JsonView(Views.CategoryCatalog.class)
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Category> findOne(@PathVariable Integer id) {
        return super.findOne(id);
    }
}

package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CategoryController{
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<HttpStatus> createNewCategory(@RequestBody CategoryDTO categoryDTO) {
        Category newCategory = categoryService.transferCategoryDtoToCategory(categoryDTO);
        categoryService.create(newCategory);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Category> getCatalog() {
        Category mainCategory = categoryService.findById(1);
        return ResponseEntity.ok(mainCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Item>> getItems(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getItemsFromCategory(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateCategory(@PathVariable Integer id,
                                                     @RequestBody CategoryDTO categoryDTO) {
        Category oldCategory = categoryService.findById(id);
        if (oldCategory == null) {
            return ResponseEntity.notFound().build(); //TODO throw
        }
        Category updateCategory = categoryService.transferCategoryDtoToCategory(categoryDTO);
        categoryService.update(oldCategory, updateCategory);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Category id) {
        categoryService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

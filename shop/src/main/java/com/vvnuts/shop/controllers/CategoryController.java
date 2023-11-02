package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.CategoryDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.services.interfaces.CategoryService;
import com.vvnuts.shop.services.interfaces.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CategoryController{
    private final CategoryService categoryService;
    private final CharacteristicService characteristicService;

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

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateCategory(@PathVariable Integer id,
                                                     @RequestBody CategoryDTO categoryDTO) {
        Category oldCategory = categoryService.findById(id);
        if (oldCategory == null) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST); //TODO throw
        }
        Category updateCategory = categoryService.transferCategoryDtoToCategory(categoryDTO);
        categoryService.update(updateCategory);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Category id) {
        categoryService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

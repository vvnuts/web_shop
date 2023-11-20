package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.services.CategoryService;
import com.vvnuts.shop.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CategoryController{
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody CategoryRequest categoryRequest){
        categoryService.create(categoryRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> findAll(){
        List<CategoryResponse> entities = categoryService.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<Item>> findItemFromCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.findItemInCategory(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Integer id,
                                             @RequestBody CategoryRequest request) {
        categoryService.update(request, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

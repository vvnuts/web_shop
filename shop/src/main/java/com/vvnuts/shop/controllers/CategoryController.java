package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.services.CategoryService;
import com.vvnuts.shop.utils.validators.CategoryValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CategoryController{
    private final CategoryService service;
    private final CategoryValidator validator;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CategoryRequest request){
        validator.validate(request);
        service.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Collection<CategoryResponse>> findAll(){
        List<CategoryResponse> entities = service.findAll();
        return ResponseEntity.ok(entities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid CategoryRequest request) {
        Category category = validator.validate(request, id);
        service.update(request, category);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

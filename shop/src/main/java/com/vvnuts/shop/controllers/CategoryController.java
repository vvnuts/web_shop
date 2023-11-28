package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.dtos.responses.erorrs.CategoryParentContainsItselfException;
import com.vvnuts.shop.dtos.responses.erorrs.CycleHasFormedException;
import com.vvnuts.shop.dtos.responses.erorrs.Violation;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.services.CategoryService;
import com.vvnuts.shop.utils.CategoryUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/catalog")
public class CategoryController{
    private final CategoryService categoryService;
    private final CategoryUtils  categoryUtils;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CategoryRequest categoryRequest){
        categoryService.create(categoryRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> findAll(){
        List<CategoryResponse> entities = categoryService.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<Item>> findItemFromCategory(@PathVariable @Min(0) Integer id) {
        return ResponseEntity.ok(categoryService.findItemInCategory(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid CategoryRequest request) {
        categoryUtils.validate(request, id);
        categoryService.update(request, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler({CategoryParentContainsItselfException.class, CycleHasFormedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onCategoryParentContainsItselfException(RuntimeException e) {
        return e.getMessage();
    }
}

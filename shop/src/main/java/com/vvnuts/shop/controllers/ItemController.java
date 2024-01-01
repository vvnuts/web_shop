package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.requests.PageRequestDto;
import com.vvnuts.shop.dtos.requests.SpecificationItemRequest;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.services.ItemService;
import com.vvnuts.shop.services.specifications.ItemSpecification;
import com.vvnuts.shop.utils.validators.ItemValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController{
    private final ItemService service;
    private final ItemSpecification specification;
    private final ItemRepository repository;
    private final ItemValidator validator;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ItemRequest request) {
        service.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Item>> findWithFilter(@RequestBody @Valid SpecificationItemRequest request) {
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        return ResponseEntity.ok(repository.findAll(specification.createSpecification(request), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findOne(@PathVariable @Min(0) Integer id) {
        Item item = service.findById(id);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                                 @RequestBody @Valid ItemRequest request) {
        Item item = validator.validate(request, id);
        service.update(request, item);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

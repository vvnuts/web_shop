package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.services.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController{
    private final ItemService itemService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ItemRequest request) {
        itemService.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findOne(@PathVariable @Min(0) Integer id) {
        Item item = itemService.findById(id);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                                 @RequestBody @Valid ItemRequest request) {
        itemService.update(request, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        itemService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

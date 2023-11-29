package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.services.CharacterItemService;
import com.vvnuts.shop.utils.validators.CharacterItemValidator;
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
@RequestMapping("/api/v1/character_item")
public class CharacterItemController {
    private final CharacterItemService service;
    private final CharacterItemValidator validator;

    @PostMapping("/{itemId}")
    public ResponseEntity<?> create(@PathVariable @Min(0) Integer itemId,
                                    @RequestBody CharacterItemRequest request){
        validator.validate(request);
        service.create(itemId, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterItem> findOne(@PathVariable @Min(0) Integer id) {
        CharacterItem characterItem = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(characterItem);
    }

    @GetMapping("item/{itemId}")
    public ResponseEntity<Collection<CharacterItem>> findAll(@PathVariable @Min(0) Integer itemId) {
        List<CharacterItem> characterItems = service.findAllCharacterItemFromItem(itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(characterItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody CharacterItemRequest request) {
        CharacterItem characterItem = validator.validate(request, id);
        service.update(characterItem, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        service.deleteCharacterItem(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

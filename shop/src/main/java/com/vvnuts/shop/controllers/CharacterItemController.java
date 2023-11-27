package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.services.CharacterItemService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/character_item")
public class CharacterItemController {
    private final CharacterItemService characterItemService;

    @PostMapping("/{itemId}")
    public ResponseEntity<?> create(@PathVariable @Min(0) Integer itemId, CharacterItemRequest request){
        characterItemService.create(itemId, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<CharacterItem>> findAll(@PathVariable @Min(0) Integer itemId) {
        List<CharacterItem> characterItems = characterItemService.findAllCharacterItemFromItem(itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(characterItems);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete (@PathVariable @Min(0) Integer id) {
        characterItemService.deleteCharacterItem(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

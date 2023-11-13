package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.ItemDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.services.interfaces.CrudService;
import com.vvnuts.shop.services.interfaces.ItemService;
import com.vvnuts.shop.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController extends AbstractCrudController<Item, ItemDTO, Integer>{
    private final ItemService itemService;

    @Override
    CrudService<Item, ItemDTO, Integer> getService() {
        return itemService;
    }

//    @PostMapping()
//    public ResponseEntity<HttpStatus> createItem(@RequestBody ItemDTO itemDTO) {
//        Item newItem = itemService.transferItemDtoToItem(itemDTO);
//        itemService.create(newItem);
//        return ResponseEntity.ok(HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Item> getItemInfo(@PathVariable Integer id) {
//        Item item = itemService.findById(id);
//        return ResponseEntity.ok(item);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<HttpStatus> updateItem(@PathVariable Integer id, @RequestBody ItemDTO itemDTO) {
//        Item updateItem = itemService.findById(id);
//        Item updateDTO = itemService.transferItemDtoToItem(itemDTO);
//        itemService.update(updateItem, updateDTO);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Item id) {
//        itemService.delete(id);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
}

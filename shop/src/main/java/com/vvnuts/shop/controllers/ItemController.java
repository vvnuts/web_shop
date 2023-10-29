package com.vvnuts.shop.controllers;

import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.services.interfaces.CrudService;
import com.vvnuts.shop.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController extends AbstractCrudController<Item, Integer>{
    private final ItemService itemService;
    @Override
    CrudService<Item, Integer> getService() {
        return itemService;
    }
}

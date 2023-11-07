package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.ItemDTO;
import com.vvnuts.shop.entities.Item;

public interface ItemService  extends CrudService<Item, Integer>{
    Item transferItemDtoToItem(ItemDTO itemDTO);
    void update(Item updateItem, Item updateDTO);
}

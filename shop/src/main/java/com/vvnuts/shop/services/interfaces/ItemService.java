package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.ItemDTO;
import com.vvnuts.shop.entities.Item;

public interface ItemService  extends CrudService<Item, ItemDTO, Integer>{
    Item transferItemDtoToItem(ItemDTO itemDTO);
}

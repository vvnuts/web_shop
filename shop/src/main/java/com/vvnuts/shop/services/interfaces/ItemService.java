package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.CreateItemRequest;
import com.vvnuts.shop.entities.Item;

public interface ItemService  extends CrudService<Item, CreateItemRequest, Integer>{
    Item transferItemDtoToItem(CreateItemRequest createItemRequest);
    void calculateRatingItem (Item item);
}

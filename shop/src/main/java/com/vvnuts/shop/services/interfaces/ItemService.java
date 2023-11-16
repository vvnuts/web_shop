package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.CreateItemRequest;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.entities.Item;

public interface ItemService  extends CrudService<Item, CreateItemRequest, Integer>{
    Item transferItemDtoToItem(CreateItemRequest createItemRequest);
    ItemResponse convertEntityToResponse(Item item);
    void calculateRatingItem (Item item);
}

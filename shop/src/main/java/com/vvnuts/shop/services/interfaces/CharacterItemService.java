package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;

public interface CharacterItemService extends CrudService<CharacterItem, Integer>{
    public void addCategoryItemsCharacteristic(Category category, Characteristic characteristic);
    public void removeCategoryItemsCharacteristic(Category category, Characteristic characteristic);
}

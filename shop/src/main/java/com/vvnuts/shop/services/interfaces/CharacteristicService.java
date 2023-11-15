package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;

import java.util.List;

public interface CharacteristicService extends CrudService<Characteristic, CharacteristicRequest, Integer>{
    List<Characteristic> transferIdsToCharacteristicList(List<Integer> ids, Category newCategory);

    List<Characteristic> getCharacteristicListFromDTO(List<CharacteristicRequest> characteristicsDTO);
}

package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.CharacteristicDTO;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;

import java.util.List;

public interface CharacteristicService extends CrudService<Characteristic, CharacteristicDTO, Integer>{
    List<Characteristic> transferIdsToCharacteristicList(List<Integer> ids, Category newCategory);

    List<Characteristic> getCharacteristicListFromDTO(List<CharacteristicDTO> characteristicsDTO);
}

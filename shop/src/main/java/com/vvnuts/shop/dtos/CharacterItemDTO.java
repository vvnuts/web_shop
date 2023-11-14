package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterItemDTO {
    private String value;
    private Integer numValue;
    private CharacteristicDTO characteristic;
    private ItemDTO item;
}

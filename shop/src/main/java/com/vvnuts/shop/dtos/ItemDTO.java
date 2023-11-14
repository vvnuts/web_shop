package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private Integer itemId;
    private Category category;
    private String itemName;
    private String description;
    private Integer quantity;
    private Integer price;
    private Float sale;
    private List<CharacterItemDTO> characterItems;
}

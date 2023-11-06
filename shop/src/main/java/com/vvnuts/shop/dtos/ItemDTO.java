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
    private Category category;
    private String itemName;
    private String description;
    private int quantity;
    private int price;
    private float sale;
    private List<CharacterItem> characterItems;
}

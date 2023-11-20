package com.vvnuts.shop.dtos.requests;

import com.vvnuts.shop.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Integer categoryId;
    private String itemName;
    private String description;
    private Integer quantity;
    private Integer price;
    private Float sale;
    private List<CharacterItemRequest> characterItems;
}

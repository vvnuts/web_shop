package com.vvnuts.shop.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private Integer itemId;
    private String itemName;
    private Integer quantity;
    private Integer price;
    private Float sale;
}

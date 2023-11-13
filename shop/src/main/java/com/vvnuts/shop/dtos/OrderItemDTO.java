package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Item item;
    private Integer quantity;
}

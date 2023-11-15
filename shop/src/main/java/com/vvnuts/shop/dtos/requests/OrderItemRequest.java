package com.vvnuts.shop.dtos.requests;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    private ItemRequest item;
    private Integer quantity;
}

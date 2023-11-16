package com.vvnuts.shop.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BucketResponse {
    private List<OrderItemResponse> orderItem;
    private UserResponse user;
    private BigDecimal totalPrice;
    private Integer totalQuantity;
}

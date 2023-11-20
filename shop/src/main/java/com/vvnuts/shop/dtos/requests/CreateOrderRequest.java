package com.vvnuts.shop.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String email;
    private List<OrderItemRequest> orderItemRequests;
    private Integer user;
}

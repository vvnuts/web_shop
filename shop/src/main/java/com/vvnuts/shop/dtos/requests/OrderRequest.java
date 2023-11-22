package com.vvnuts.shop.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @Email
    private String email;
    @Valid
    private List<OrderItemRequest> orderItemRequests;
    private Integer user;
}

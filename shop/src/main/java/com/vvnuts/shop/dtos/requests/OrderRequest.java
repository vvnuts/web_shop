package com.vvnuts.shop.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    @Email
    private String email;

    @Valid
    private List<OrderItemRequest> orderItemRequests;

    @NotNull
    @Min(0)
    private Integer user;
}

package com.vvnuts.shop.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    @NotNull
    @Min(0)
    private Integer item;

    @NotNull
    @Min(1)
    private Integer quantity;
}

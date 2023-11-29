package com.vvnuts.shop.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class BucketRequest {
    @Valid
    private List<OrderItemRequest> orderItem;

    @NotNull
    @Min(0)
    private Integer user;
}

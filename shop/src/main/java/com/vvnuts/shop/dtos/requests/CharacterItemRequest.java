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
public class CharacterItemRequest {
    private String value;

    private Integer numValue;

    @NotNull
    @Min(0)
    private Integer characteristic;
}

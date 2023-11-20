package com.vvnuts.shop.dtos.requests;

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
    private Integer characteristic;
}

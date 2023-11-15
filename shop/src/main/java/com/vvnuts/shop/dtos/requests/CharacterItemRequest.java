package com.vvnuts.shop.dtos.requests;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.dtos.requests.CreateItemRequest;
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
    private CharacteristicRequest characteristic;
    private CreateItemRequest item;
}

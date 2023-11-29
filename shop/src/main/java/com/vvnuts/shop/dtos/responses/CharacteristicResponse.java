package com.vvnuts.shop.dtos.responses;

import com.vvnuts.shop.entities.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicResponse {
    private Integer id;
    private Type type;
    private String name;
}

package com.vvnuts.shop.dtos.requests;

import com.vvnuts.shop.entities.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicRequest {
    @NotBlank
    private String name;

    private Type type;

    @NotEmpty
    private List<Integer> categories;
}

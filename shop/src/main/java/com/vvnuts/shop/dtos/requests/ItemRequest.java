package com.vvnuts.shop.dtos.requests;

import com.vvnuts.shop.entities.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @NotNull
    @Min(0)
    private Integer categoryId;

    @NotBlank
    @Size(min = 3, max = 256)
    private String itemName;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @Min(1)
    private Integer price;

    @NotNull
    @Min(0)
    @Max(1)
    private Float sale;

    @Valid
    private List<CharacterItemRequest> characterItems;
}

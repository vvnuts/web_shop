package com.vvnuts.shop.dtos.requests;

import com.vvnuts.shop.entities.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private Integer categoryId;
    @NotBlank
    @Size(max = 256)
    private String itemName;
    @NotBlank
    private String description;
    @Min(0)
    private Integer quantity;
    @Min(1)
    private Integer price;
    @Min(0)
    @Max(1)
    private Float sale;
    private List<CharacterItemRequest> characterItems;
}

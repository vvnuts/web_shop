package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String categoryName;
    private List<CategoryDTO> parents;
    private List<CharacteristicDTO> characteristics;
}

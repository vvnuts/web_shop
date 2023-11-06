package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private String categoryName;
    private List<Category> parents;
    private List<Characteristic> characteristics;
}

package com.vvnuts.shop.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private String categoryName;
    private List<Integer> parentsId;
}

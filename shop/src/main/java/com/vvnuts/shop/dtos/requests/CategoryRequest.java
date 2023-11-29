package com.vvnuts.shop.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank
    private String categoryName;

    @NotEmpty
    private Set<Integer> parents;

    @NotEmpty
    private Set<Integer> characteristics;
}

package com.vvnuts.shop.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {
    @Min(1)
    @Max(5)
    private Integer mark;
    @NotBlank
    @Size(min = 10, max = 1000)
    private String text;
}

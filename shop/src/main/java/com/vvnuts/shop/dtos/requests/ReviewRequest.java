package com.vvnuts.shop.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer mark;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String text;

    @NotNull
    @Min(0)
    private Integer item;

    @NotNull
    @Min(0)
    private Integer user;
}

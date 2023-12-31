package com.vvnuts.shop.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationItemRequest {
    @NotNull
    private Integer categoryId;

    @Pattern(regexp = "\\d+ - \\d+")
    private String rangeOfPrice;

    private Map<String, String> characteristicValue;

    @Pattern(regexp = "\\d - \\d") //TODO check
    private String rangeOfMark;

    private PageRequestDto pageRequest;
}

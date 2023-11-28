package com.vvnuts.shop.dtos.responses.erorrs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundResponse {
    private String message;
}

package com.vvnuts.shop.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private Integer mark;
    private String text;
    private Integer item;
    private UserRequest user;
}

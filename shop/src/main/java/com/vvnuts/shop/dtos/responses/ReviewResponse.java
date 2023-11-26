package com.vvnuts.shop.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Integer id;
    private Integer mark;
    private String text;
    private List<Integer> reviewImage;
    private ItemResponse item;
    private UserResponse user;
}

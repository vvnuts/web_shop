package com.vvnuts.shop.dtos.responses;

import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Integer id;
    private Integer mark;
    private String text;
    private ItemResponse item;
    private UserResponse user;
}

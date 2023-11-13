package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer mark;
    private String text;
    private Item item;
    private User user;
}

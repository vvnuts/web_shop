package com.vvnuts.shop.dtos;

import com.vvnuts.shop.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String email;
    private List<OrderItemDTO> orderItemDTOs;
    private User user;
}

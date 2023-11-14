package com.vvnuts.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BucketDTO {
    private List<OrderItemDTO> orderItem;
    private UserDTO user;
}

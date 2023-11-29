package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    private final UserService userService;

    public Order transferToCreateEntity(OrderRequest dto) {
        return Order.builder()
                .email(dto.getEmail())
                .orderItems(orderItemMapper.transferOrderItemDtoToList(dto.getOrderItemRequests()))
                .user(userService.findById(dto.getUser()))
                .status(Status.WAITING)
                .build();
    }
}

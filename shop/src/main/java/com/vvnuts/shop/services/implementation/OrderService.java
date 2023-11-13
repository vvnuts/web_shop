package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.OrderDTO;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.repositories.OrderRepository;
import com.vvnuts.shop.services.OrderItemService;
import com.vvnuts.shop.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;

    public void create(OrderDTO dtoEntity) {
        Order newOrder = transferToCreateEntity(dtoEntity);
        calculationQuantityAndPrice(newOrder);
        orderRepository.save(newOrder);
    }

    public void approveOrder(Order order) {
        order.setStatus(Status.SUCCESS);
    }

    public void cancelingOrder(Order order) {
        order.setStatus(Status.CANCEL);
    }

    Order transferToCreateEntity(OrderDTO dto) {
        return Order.builder()
                .email(dto.getEmail())
                .orderItems(orderItemService.transferOrderItemDtoToList(dto.getOrderItemDTOs()))
                .user(userService.findById(dto.getUser().getUserId()))
                .status(Status.WAITING)
                .build();
    }

    private void calculationQuantityAndPrice(Order order) {
        BigDecimal totalSum = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        for (OrderItem orderItem: order.getOrderItems()) {
            totalSum = totalSum.add(BigDecimal.valueOf(orderItem.getItem().getPrice() * (1 + orderItem.getItem().getSale())));
            totalQuantity += orderItem.getQuantity();
        }
        order.setTotalPrice(totalSum);
        order.setTotalQuantity(totalQuantity);
    }
}

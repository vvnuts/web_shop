package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CreateOrderRequest;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;

    public void create(CreateOrderRequest dtoEntity) {
        Order newOrder = transferToCreateEntity(dtoEntity);
        calculationQuantityAndPrice(newOrder);
        orderRepository.save(newOrder);
    }

    public void approveOrder(Integer orderId) {
        Order order = findById(orderId);
        if (order == null) {
            return; //TODO throw
        }
        //TODO списать товары
        order.setStatus(Status.SUCCESS);
    }

    public void cancelingOrder(Integer orderId) {
        Order order = findById(orderId);
        if (order == null) {
            return; //TODO throw
        }
        order.setStatus(Status.CANCEL);
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }


    Order transferToCreateEntity(CreateOrderRequest dto) {
        return Order.builder()
                .email(dto.getEmail())
                .orderItems(orderItemService.transferOrderItemDtoToList(dto.getOrderItemRequests()))
                .user(userService.findById(dto.getUser()))
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

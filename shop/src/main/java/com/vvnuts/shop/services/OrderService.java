package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final BucketService bucketService;

    public Order create(OrderRequest dtoEntity) {
        Order newOrder = transferToCreateEntity(dtoEntity);
        calculationQuantityAndPrice(newOrder);
        return orderRepository.save(newOrder);
    }

    public void approveOrder(UUID orderId) {
        Order order = findById(orderId);
        for (OrderItem orderItem: order.getOrderItems()) {
            if (orderItem.getQuantity() > orderItem.getItem().getQuantity()) {
                order.setStatus(Status.CANCEL);
                orderRepository.save(order);
                return; // TODO throw
            }
        }
        for (OrderItem orderItem: order.getOrderItems()) {
            Item temp = orderItem.getItem();
            temp.setQuantity(temp.getQuantity() - orderItem.getQuantity());
            itemRepository.save(temp);
        }
        bucketService.removeItemFromBucket(order.getUser());
        order.setStatus(Status.SUCCESS);
        orderRepository.save(order);
    }

    public void cancelingOrder(UUID orderId) {
        Order order = findById(orderId);
        order.setStatus(Status.CANCEL);
        orderRepository.save(order);
    }

    public void delete(UUID id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id).orElseThrow();
    }


    Order transferToCreateEntity(OrderRequest dto) {
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
            totalSum = totalSum.add(BigDecimal.valueOf(orderItem.getQuantity() * orderItem.getItem().getPrice() *
                    (1 + orderItem.getItem().getSale())));
            totalQuantity += orderItem.getQuantity();
        }
        order.setTotalPrice(totalSum);
        order.setTotalQuantity(totalQuantity);
    }
}

package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.repositories.OrderRepository;
import com.vvnuts.shop.utils.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final ItemRepository itemRepository;
    private final OrderMapper mapper;
    private final BucketService bucketService;

    public Order create(OrderRequest dtoEntity) {
        Order newOrder = mapper.transferToCreateEntity(dtoEntity);
        calculationQuantityAndPrice(newOrder);
        return repository.save(newOrder);
    }

    public void approveOrder(UUID orderId) {
        Order order = findById(orderId);
        for (OrderItem orderItem: order.getOrderItems()) {
            Item temp = orderItem.getItem();
            temp.setQuantity(temp.getQuantity() - orderItem.getQuantity());
            itemRepository.save(temp);
        }
        bucketService.removeItemFromBucket(order.getUser());
        order.setStatus(Status.SUCCESS);
        repository.save(order);
    }

    public void cancelingOrder(UUID orderId) {
        Order order = findById(orderId);
        order.setStatus(Status.CANCEL);
        repository.save(order);
    }

    public void delete(UUID id) {
        Order order = findById(id);
        repository.delete(order);
    }

    public Order findById(UUID id) {
        return repository.findById(id).orElseThrow();
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

package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.repositories.OrderItemRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;

    public List<OrderItem> transferOrderItemDtoToList(List<OrderItemRequest> orderItemsDTO) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemsDTO) {
            OrderItem orderItem = OrderItem.builder()
                    .item(itemService.findById(orderItemRequest.getItem().getItemId()))
                    .quantity(orderItemRequest.getQuantity())
                    .build();
            if (orderItem.getItem().getQuantity() < orderItem.getQuantity()) {
                return null; //TODO exception
            }
            orderItemRepository.save(orderItem); //TODO убрать
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}

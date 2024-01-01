package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.repositories.OrderItemRepository;
import com.vvnuts.shop.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemMapper {
    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;

    public List<OrderItem> transferOrderItemDtoToList(List<OrderItemRequest> orderItemsDTO) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemsDTO) {
            OrderItem orderItem = OrderItem.builder()
                    .item(itemService.findById(orderItemRequest.getItem()))
                    .quantity(orderItemRequest.getQuantity())
                    .build();
            if (orderItem.getItem().getQuantity() < orderItem.getQuantity()) {
                throw new RuntimeException("Больше заявленного кол-ва");
            }
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}

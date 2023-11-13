package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.OrderItemDTO;
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

    public List<OrderItem> transferOrderItemDtoToList(List<OrderItemDTO> orderItemsDTO) {
        List<OrderItem> bucketItems = new ArrayList<>();
        for (OrderItemDTO orderItemDTO: orderItemsDTO) {
            OrderItem orderItem = OrderItem.builder()
                    .item(itemService.findById(orderItemDTO.getItem().getItemId()))
                    .quantity(orderItemDTO.getQuantity())
                    .build();
            if (orderItem.getItem().getQuantity() < orderItem.getQuantity()) {
                return null; //TODO exception
            }
            orderItemRepository.save(orderItem); //TODO убрать
            bucketItems.add(orderItem);
        }
        return bucketItems;
    }
}

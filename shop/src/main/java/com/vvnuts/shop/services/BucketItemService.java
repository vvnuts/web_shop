package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.OrderItemDTO;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketItemRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketItemService {
    private final BucketItemRepository bucketItemRepository;
    private final ItemService itemService;

    public List<BucketItem> transferBucketItemDtoToList(List<OrderItemDTO> orderItemsDTO) {
        List<BucketItem> bucketItems = new ArrayList<>();
        for (OrderItemDTO orderItemDTO: orderItemsDTO) {
            BucketItem bucketItem = BucketItem.builder()
                    .item(itemService.findById(orderItemDTO.getItem().getItemId()))
                    .quantity(orderItemDTO.getQuantity())
                    .build();
            if (bucketItem.getItem().getQuantity() < bucketItem.getQuantity()) {
                return null; //TODO exception
            }
            bucketItemRepository.save(bucketItem); //TODO убрать
            bucketItems.add(bucketItem);
        }
        return bucketItems;
    }
}

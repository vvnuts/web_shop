package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.dtos.responses.OrderItemResponse;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketItemMapper {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public List<BucketItem> transferBucketItemDtoToList(List<OrderItemRequest> orderItemsDTO) {
        List<BucketItem> bucketItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemsDTO) {
            BucketItem bucketItem = BucketItem.builder()
                    .item(itemService.findById(orderItemRequest.getItem()))
                    .quantity(orderItemRequest.getQuantity())
                    .build();
            bucketItems.add(bucketItem);
        }
        return bucketItems;
    }

    public List<OrderItemResponse> convertEntityToListResponse (List<BucketItem> bucketItems) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (BucketItem bucketItem: bucketItems) {
            orderItemResponses.add(convertEntityToResponse(bucketItem));
        }
        return orderItemResponses;
    }

    public OrderItemResponse convertEntityToResponse (BucketItem bucketItem) {
        return OrderItemResponse.builder()
                .item(itemMapper.convertEntityToResponse(bucketItem.getItem()))
                .quantity(bucketItem.getQuantity())
                .build();
    }
}

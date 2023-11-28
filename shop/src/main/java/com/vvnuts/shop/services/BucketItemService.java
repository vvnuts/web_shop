package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.dtos.responses.OrderItemResponse;
import com.vvnuts.shop.dtos.responses.erorrs.NoQuantityEnoughException;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketItemService {
    private final BucketItemRepository bucketItemRepository;
    private final ItemService itemService;

    public List<BucketItem> transferBucketItemDtoToList(List<OrderItemRequest> orderItemsDTO) {
        List<BucketItem> bucketItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemsDTO) {
            BucketItem bucketItem = BucketItem.builder()
                    .item(itemService.findById(orderItemRequest.getItem()))
                    .quantity(orderItemRequest.getQuantity())
                    .build();
            if (bucketItem.getItem().getQuantity() < bucketItem.getQuantity()) {
                throw new NoQuantityEnoughException("'Продукции '" + bucketItem.getItem().getItemName() + "' недостаточно на складе");
            }
            bucketItems.add(bucketItem);
        }
        return bucketItems;
    }

    public void linkBucket(Bucket bucket) {
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            bucketItem.setBucket(bucket);
            bucketItemRepository.save(bucketItem);
        }
    }

    public void removeBucket(BucketItem bucketItem) {
        bucketItem.setBucket(null);
        bucketItemRepository.save(bucketItem);
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
                .item(itemService.convertEntityToResponse(bucketItem.getItem()))
                .quantity(bucketItem.getQuantity())
                .build();
    }
}

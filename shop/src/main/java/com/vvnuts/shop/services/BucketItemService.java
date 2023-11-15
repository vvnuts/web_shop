package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketItemRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
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
                    .item(itemService.findById(orderItemRequest.getItem().getItemId()))
                    .quantity(orderItemRequest.getQuantity())
                    .build();
            if (bucketItem.getItem().getQuantity() < bucketItem.getQuantity()) {
                return null; //TODO exception
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
}

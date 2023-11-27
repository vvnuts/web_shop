package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final BucketItemService bucketItemService;
    private final UserService userService;

    public void update(BucketRequest request, Integer id) {
        Bucket updateBucket = findBucketByUserId(id);
        if (updateBucket.getBucketItems() != null) {
            for (BucketItem bucketItem: updateBucket.getBucketItems()) {
                bucketItemService.removeBucket(bucketItem);
            }
        }
        updateBucket.setBucketItems(bucketItemService.transferBucketItemDtoToList(request.getOrderItem()));
        bucketItemService.linkBucket(updateBucket);
        calculationQuantityAndPrice(updateBucket);
        bucketRepository.save(updateBucket);
    }

    public Bucket findBucketByUserId(Integer userId) {
        return bucketRepository.findByUser(userService.findById(userId)).orElseThrow();
    }

    private void calculationQuantityAndPrice(Bucket bucket) {
        BigDecimal totalSum = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            totalSum = totalSum.add(BigDecimal.valueOf(bucketItem.getQuantity() * bucketItem.getItem().getPrice()
                    * (1 + bucketItem.getItem().getSale())));
            totalQuantity += bucketItem.getQuantity();
        }
        bucket.setTotalPrice(totalSum);
        bucket.setTotalQuantity(totalQuantity);
    }

    public BucketResponse findById(Integer id) {
        return convertEntityToResponse(bucketRepository.findByUser(userService.findById(id)).orElseThrow());
    }

    public BucketResponse convertEntityToResponse(Bucket bucket) {
        return BucketResponse.builder()
                .orderItem(bucketItemService.convertEntityToListResponse(bucket.getBucketItems()))
                .user(userService.convertEntityToResponse(bucket.getUser()))
                .totalPrice(bucket.getTotalPrice())
                .totalQuantity(bucket.getTotalQuantity())
                .build();
    }

    public void removeItemFromBucket(User user) {
        Bucket bucket = bucketRepository.findByUser(user).orElseThrow();
        if (bucket.getBucketItems() != null) {
            for (BucketItem bucketItem: bucket.getBucketItems()) {
                bucketItemService.removeBucket(bucketItem);
            }
        }
        bucket.setTotalQuantity(0);
        bucket.setTotalPrice(BigDecimal.ZERO);
        bucketRepository.save(bucket);
    }
}

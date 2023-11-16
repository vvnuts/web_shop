package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.services.BucketItemService;
import com.vvnuts.shop.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final BucketItemService bucketItemService;
    private final UserService userService;

    public void update(BucketRequest request, Integer id) {
        Bucket updateBucket = findBucketByUserId(id);
        for (BucketItem bucketItem: updateBucket.getBucketItems()) {
            bucketItemService.removeBucket(bucketItem);
        }
        updateBucket.setBucketItems(bucketItemService.transferBucketItemDtoToList(request.getOrderItem()));
        bucketItemService.linkBucket(updateBucket);
        calculationQuantityAndPrice(updateBucket);
        bucketRepository.save(updateBucket);
    }

    public void create(BucketRequest request) {
        Bucket bucket = Bucket.builder()
                .user(userService.findById(request.getUser().getUserId()))
                .bucketItems(bucketItemService.transferBucketItemDtoToList(request.getOrderItem()))
                .build();
        bucketItemService.linkBucket(bucket);
        calculationQuantityAndPrice(bucket);
        bucketRepository.save(bucket);
    }

    public Bucket findBucketByUserId(Integer userId) {
        return bucketRepository.findByUser(userService.findById(userId));
    }

    private void calculationQuantityAndPrice(Bucket bucket) {
        BigDecimal totalSum = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            totalSum = totalSum.add(BigDecimal.valueOf(bucketItem.getItem().getPrice() * (1 + bucketItem.getItem().getSale())));
            totalQuantity += bucketItem.getQuantity();
        }
        bucket.setTotalPrice(totalSum);
        bucket.setTotalQuantity(totalQuantity);
    }

    public BucketResponse findById(Integer id) {
        Bucket bucket = bucketRepository.findByUser(userService.findById(id));

        return convertEntityToResponse(bucketRepository.findByUser(userService.findById(id)));
    }

    public BucketResponse convertEntityToResponse(Bucket bucket) {
        return BucketResponse.builder()
                .orderItem(bucketItemService.convertEntityToListResponse(bucket.getBucketItems()))
                .user(userService.convertEntityToResponse(bucket.getUser()))
                .totalPrice(bucket.getTotalPrice())
                .totalQuantity(bucket.getTotalQuantity())
                .build();
    }
}

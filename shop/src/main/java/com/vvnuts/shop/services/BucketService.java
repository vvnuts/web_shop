package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.utils.mappers.BucketItemMapper;
import com.vvnuts.shop.utils.mappers.BucketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository repository;
    private final BucketMapper mapper;
    private final BucketItemMapper bucketItemMapper;
    private final BucketItemService bucketItemService;
    private final UserService userService;

    public void create(BucketRequest request) {
        Bucket bucket = Bucket.builder()
                .user(userService.findById(request.getUser()))
                .build();
        bucket.setBucketItems(bucketItemMapper.transferBucketItemDtoToList(request.getOrderItem()));
        calculationQuantityAndPrice(bucket);
        repository.save(bucket);
        bucketItemService.linkBucket(bucket);
    }

    public BucketResponse findOne(Integer userId) {
        Bucket bucket = findBucketByUserId(userId);
        return mapper.convertEntityToResponse(bucket);
    }

    public Bucket findBucketByUserId(Integer userId) {
        return repository.findByUser(userService.findById(userId)).orElseThrow();
    }

    public void update(BucketRequest request, Bucket updateBucket) {
        if (updateBucket.getBucketItems() != null) {
            for (BucketItem bucketItem : updateBucket.getBucketItems()) {
                bucketItemService.removeBucket(bucketItem);
            }
        }
        updateBucket.setBucketItems(bucketItemMapper.transferBucketItemDtoToList(request.getOrderItem()));
        bucketItemService.linkBucket(updateBucket);
        calculationQuantityAndPrice(updateBucket);
        repository.save(updateBucket);
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

    public void removeItemFromBucket(User user) {
        Bucket bucket = repository.findByUser(user).orElseThrow();
        if (bucket.getBucketItems() != null) {
            for (BucketItem bucketItem: bucket.getBucketItems()) {
                bucketItemService.removeBucket(bucketItem);
            }
        }
        bucket.setTotalQuantity(0);
        bucket.setTotalPrice(BigDecimal.ZERO);
        repository.save(bucket);
    }
}

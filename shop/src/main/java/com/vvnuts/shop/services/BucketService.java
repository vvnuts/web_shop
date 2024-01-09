package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.BucketItemRepository;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.utils.mappers.BucketItemMapper;
import com.vvnuts.shop.utils.mappers.BucketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository repository;
    private final BucketMapper mapper;
    private final BucketItemMapper bucketItemMapper;
    private final BucketItemService bucketItemService;
    private final BucketItemRepository bucketItemRepository;
    private final UserService userService;

    public Bucket create(BucketRequest request) {
        Bucket bucket = Bucket.builder()
                .user(userService.findById(request.getUser()))
                .build();
        bucket.setBucketItems(bucketItemMapper.transferBucketItemDtoToList(request.getOrderItem()));
        calculationQuantityAndPrice(bucket);
        bucket = repository.save(bucket);
        bucketItemService.linkBucket(bucket);
        return bucket;
    }

    public BucketResponse findOne(Integer userId) {
        Bucket bucket = findBucketByUserId(userId);
        return mapper.convertEntityToResponse(bucket);
    }

    public Bucket findBucketByUserId(Integer userId) {
        return repository.findByUser(userService.findById(userId)).orElseThrow();
    }

    public Bucket update(BucketRequest request, Bucket updateBucket) {
        if (updateBucket.getBucketItems() != null) {
            for (BucketItem bucketItem : updateBucket.getBucketItems()) {
                bucketItemService.removeBucket(bucketItem);
            }
        }
        updateBucket.setBucketItems(bucketItemMapper.transferBucketItemDtoToList(request.getOrderItem()));
        bucketItemService.linkBucket(updateBucket);
        calculationQuantityAndPrice(updateBucket);
        return repository.save(updateBucket);
    }

    public Bucket addItemToBucket(OrderItemRequest request, Bucket bucket) {
        BucketItem addItem = bucketItemMapper.transferBucketItemDtoToEntity(request);
        bucket.getBucketItems().add(addItem);
        addItem.setBucket(bucket);
        bucketItemRepository.save(addItem);
        bucket.setTotalQuantity(bucket.getTotalQuantity() + addItem.getQuantity());
        BigDecimal sale = BigDecimal.ONE.subtract(BigDecimal.valueOf(addItem.getItem().getSale()));
        BigDecimal price = BigDecimal.valueOf(addItem.getItem().getPrice()).multiply(sale).multiply(BigDecimal.valueOf(request.getQuantity()));
        bucket.setTotalPrice(bucket.getTotalPrice().add(price));
        return repository.save(bucket);
    }

    private void calculationQuantityAndPrice(Bucket bucket) {
        BigDecimal totalSum = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            BigDecimal priceWithoutSale = BigDecimal.valueOf(bucketItem.getQuantity()).multiply(BigDecimal.valueOf(bucketItem.getItem().getPrice()));
            BigDecimal sale = BigDecimal.ONE.subtract(BigDecimal.valueOf(bucketItem.getItem().getSale()));
            BigDecimal price = priceWithoutSale.multiply(sale);
            totalSum = totalSum.add(price);
            totalQuantity += bucketItem.getQuantity();
        }
        bucket.setTotalPrice(totalSum.setScale(1, RoundingMode.HALF_UP));
        bucket.setTotalQuantity(totalQuantity);
    }

    public Bucket removeItemFromBucket(User user) {
        Bucket bucket = repository.findByUser(user).orElseThrow();
        if (bucket.getBucketItems() != null) {
            for (BucketItem bucketItem: bucket.getBucketItems()) {
                bucketItemService.removeBucket(bucketItem);
            }
        }
        bucket.setTotalQuantity(0);
        bucket.setTotalPrice(BigDecimal.ZERO);
        return repository.save(bucket);
    }
}

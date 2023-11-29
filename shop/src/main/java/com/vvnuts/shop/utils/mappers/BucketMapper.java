package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketMapper {
    private final UserMapper userMapper;
    private final BucketItemMapper bucketItemMapper;

    public BucketResponse convertEntityToResponse(Bucket bucket) {
        return BucketResponse.builder()
                .orderItem(bucketItemMapper.convertEntityToListResponse(bucket.getBucketItems()))
                .user(userMapper.convertEntityToResponse(bucket.getUser()))
                .totalPrice(bucket.getTotalPrice())
                .totalQuantity(bucket.getTotalQuantity())
                .build();
    }
}

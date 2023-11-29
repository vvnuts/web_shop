package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketItemRepository;
import com.vvnuts.shop.utils.mappers.BucketItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketItemService {
    private final BucketItemRepository repository;
    private final BucketItemMapper mapper;

    public void linkBucket(Bucket bucket) {
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            bucketItem.setBucket(bucket);
            repository.save(bucketItem);
        }
    }

    public void removeBucket(BucketItem bucketItem) {
        bucketItem.setBucket(null);
        repository.save(bucketItem);
    }
}

package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketItemService {
    private final BucketItemRepository repository;

    public void linkBucket(Bucket bucket) {
        if (bucket.getBucketItems() == null) {
            return;
        }
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            bucketItem.setBucket(bucket);
            repository.save(bucketItem);
        }
    }

    public void removeBucket(BucketItem bucketItem) {
        if (bucketItem.getBucket() == null) {
            return;
        }
        bucketItem.setBucket(null);
        repository.save(bucketItem);
    }
}

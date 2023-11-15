package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.entities.Bucket;

public interface BucketService extends CrudService<Bucket, BucketRequest, Integer>{
    Bucket findBucketByUserId(Integer userId);
}

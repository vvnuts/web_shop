package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.services.interfaces.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketServiceImplementation extends AbstractCrudService<Bucket, Integer> implements BucketService {
    private final BucketRepository bucketRepository;
    @Override
    JpaRepository<Bucket, Integer> getRepository() {
        return bucketRepository;
    }
}

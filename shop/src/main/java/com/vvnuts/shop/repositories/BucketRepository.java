package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Integer> {
}

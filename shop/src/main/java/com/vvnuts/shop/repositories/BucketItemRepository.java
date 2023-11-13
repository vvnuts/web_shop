package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.BucketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketItemRepository extends JpaRepository<BucketItem, Integer> {
}

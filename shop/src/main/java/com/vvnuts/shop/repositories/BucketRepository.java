package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Integer> {
    Optional<Bucket> findByUser(User user);
}

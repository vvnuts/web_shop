package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {
}

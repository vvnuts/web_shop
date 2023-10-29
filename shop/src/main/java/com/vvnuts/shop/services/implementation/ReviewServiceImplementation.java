package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementation extends AbstractCrudService<Review, Integer> implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    JpaRepository<Review, Integer> getRepository() {
        return reviewRepository;
    }
}

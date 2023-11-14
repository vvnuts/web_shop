package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.ReviewDTO;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.services.interfaces.CrudService;
import com.vvnuts.shop.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController extends AbstractCrudController<Review, ReviewDTO, Integer>{
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @Override
    CrudService<Review, ReviewDTO, Integer> getService() {
        return reviewService;
    }

    private ReviewResponse convertReviewToReviewResponse(Review review) {
        return modelMapper.map(review, ReviewResponse.class);
    }
}

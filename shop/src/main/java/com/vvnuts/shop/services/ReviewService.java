package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.mappers.ItemMapper;
import com.vvnuts.shop.utils.mappers.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService{
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;
    private final ReviewMapper mapper;

    public void create(ReviewRequest request) {
        Review review = mapper.transferDtoToEntity(request);
        reviewRepository.save(review);
        itemService.calculateRatingItem(review.getItem());
    }

    public ReviewResponse findOne(Integer id) {
        return mapper.convertEntityToResponse(reviewRepository.findById(id).orElseThrow());
    }

    public Review findById(Integer id) {
        return reviewRepository.findById(id).orElseThrow();
    }

    public List<ReviewResponse> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review: reviews) {
            reviewResponses.add(mapper.convertEntityToResponse(review));
        }
        return reviewResponses;
    }

    public void update(ReviewUpdateRequest request, Integer id) {
        Review updateReview = findById(id);
        updateReview.setMark(request.getMark());
        updateReview.setText(request.getText());
        reviewRepository.save(updateReview);
        itemService.calculateRatingItem(updateReview.getItem());
    }

    public void delete(Integer id) {
        Review review = findById(id);
        reviewRepository.delete(review);
    }
}

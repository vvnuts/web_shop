package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService{
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final ReviewImageService reviewImageService;

    public void create(ReviewRequest request) {
        Review review = Review.builder()
                .mark(request.getMark())
                .item(itemService.findById(request.getItem()))
                .text(request.getText())
                .user(userService.findById(request.getUser()))
                .build();
        reviewRepository.save(review);
        itemService.calculateRatingItem(review.getItem());
    }

    public ReviewResponse findById(Integer id) {
        return convertEntityToResponse(reviewRepository.findById(id).orElseThrow());
    }

    public List<ReviewResponse> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review: reviews) {
            reviewResponses.add(convertEntityToResponse(review));
        }
        return reviewResponses;
    }

    public void update(ReviewUpdateRequest dtoEntity, Integer id) {
        Review updateReview = reviewRepository.findById(id).orElseThrow();
        updateReview.setMark(dtoEntity.getMark());
        updateReview.setText(dtoEntity.getText());
        reviewRepository.save(updateReview);
        itemService.calculateRatingItem(updateReview.getItem());
    }

    public void delete(Integer id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        reviewRepository.delete(review);
    }

    public ReviewResponse convertEntityToResponse(Review review) {
        return ReviewResponse.builder()
                .mark(review.getMark())
                .text(review.getText())
                .reviewImage(reviewImageService.convertEntityToListInteger(review.getImages()))
                .user(userService.convertEntityToResponse(review.getUser()))
                .item(itemService.convertEntityToResponse(review.getItem()))
                .build();
    }
}

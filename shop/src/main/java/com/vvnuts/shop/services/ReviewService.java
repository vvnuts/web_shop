package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
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

    Review transferToUpdateEntity(ReviewUpdateRequest dto, Review updateEntity) {
        updateEntity.setMark(dto.getMark());
        updateEntity.setText(dto.getText());
        return updateEntity;
    }

    Review transferToCreateEntity(ReviewRequest dto) {
        return Review.builder()
                .mark(dto.getMark())
                .item(itemService.findById(dto.getItem().getItemId()))
                .text(dto.getText())
                .user(userService.findById(dto.getUser().getUserId()))
                .build();
    }

    public void create(ReviewRequest dtoEntity) {
        Review review = transferToCreateEntity(dtoEntity);
        reviewRepository.save(review);
        itemService.calculateRatingItem(review.getItem());
    }

    public Review findById(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviewRepository.findAll());
    }

    public void update(ReviewUpdateRequest dtoEntity, Integer id) {
        Review review = transferToUpdateEntity(dtoEntity, findById(id));
        reviewRepository.save(review);
        itemService.calculateRatingItem(review.getItem());
    }

    public void delete(Review entity) {
        reviewRepository.delete(entity);
    }
}

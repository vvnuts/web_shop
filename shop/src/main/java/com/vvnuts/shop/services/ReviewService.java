package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.mappers.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService{
    private final ReviewRepository repository;
    private final ItemService itemService;
    private final ReviewMapper mapper;

    public Review create(ReviewRequest request) {
        Review review = mapper.transferDtoToEntity(request);
        Review savedReview = repository.save(review);
        itemService.calculateRatingItem(review.getItem());
        return savedReview;
    }

    public ReviewResponse findOne(Integer id) {
        return mapper.convertEntityToResponse(repository.findById(id).orElseThrow());
    }

    public Review findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public List<ReviewResponse> findAll() {
        return mapper.convertListEntityToResponse(repository.findAll());
    }

    public Review update(ReviewUpdateRequest request, Integer id) {
        Review updateReview = findById(id);
        updateReview.setMark(request.getMark());
        updateReview.setText(request.getText());
        updateReview = repository.save(updateReview);
        itemService.calculateRatingItem(updateReview.getItem());
        return updateReview;
    }

    public void delete(Integer id) {
        Review review = findById(id);
        repository.delete(review);
    }
}

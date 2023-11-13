package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.ReviewDTO;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
import com.vvnuts.shop.services.interfaces.ReviewService;
import com.vvnuts.shop.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementation extends AbstractCrudService<Review, ReviewDTO, Integer> implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;
    private final UserService userService;
    @Override
    JpaRepository<Review, Integer> getRepository() {
        return reviewRepository;
    }

    @Override
    Review transferToUpdateEntity(ReviewDTO dto, Review updateEntity) {
        return null;
    }

    @Override
    Review transferToCreateEntity(ReviewDTO dto) {
        return Review.builder()
                .mark(dto.getMark())
                .item(itemService.findById(dto.getItem().getItemId()))
                .text(dto.getText())
                .user(userService.findById(dto.getUser().getUserId()))
                .build();
    }
    @Override
    public void create(ReviewDTO dtoEntity) {
        Review review = transferToCreateEntity(dtoEntity);
        reviewRepository.save(review);
        itemService.calculateRatingItem(review.getItem());
    }
}

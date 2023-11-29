package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.services.ItemService;
import com.vvnuts.shop.services.ReviewImageService;
import com.vvnuts.shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMapper {
    private final ItemService itemService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;


    public Review transferDtoToEntity(ReviewRequest request) {
        return Review.builder()
                .mark(request.getMark())
                .item(itemService.findById(request.getItem()))
                .text(request.getText())
                .user(userService.findById(request.getUser()))
                .build();
    }

    public ReviewResponse convertEntityToResponse(Review review) {
        return ReviewResponse.builder()
                .mark(review.getMark())
                .text(review.getText())
                .user(userMapper.convertEntityToResponse(review.getUser()))
                .item(itemMapper.convertEntityToResponse(review.getItem()))
                .build();
    }
}

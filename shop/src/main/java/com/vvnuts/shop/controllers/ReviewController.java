package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.ReviewDTO;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.services.interfaces.CrudService;
import com.vvnuts.shop.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController extends AbstractCrudController<Review, ReviewDTO, Integer>{
    private final ReviewService reviewService;

    @Override
    CrudService<Review, ReviewDTO, Integer> getService() {
        return reviewService;
    }
}

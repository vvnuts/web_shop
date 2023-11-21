package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.responses.ReviewImageResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.ReviewImageRepository;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    public void uploadImage(MultipartFile file, Integer reviewId) throws IOException {
        if (file.isEmpty()){
            return; //TODO throw
        }
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        if (review.getImages() == null) {
            List<ReviewImage> reviewImages = new ArrayList<>();
            review.setImages(reviewImages);
        }
        ReviewImage newImage = ReviewImage.builder()
                .image(ImageUtils.compressImage(file.getBytes()))
                .review(review)
                .build();
        review.getImages().add(newImage);
        reviewRepository.save(review);
    }

    public List<ReviewImageResponse> downloadImages(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        List<ReviewImageResponse> response = new ArrayList<>();
        for (ReviewImage image: review.getImages()) {
            ReviewImageResponse temp = ReviewImageResponse.builder()
                    .image(ImageUtils.decompressImage(image.getImage()))
                    .build();
            response.add(temp);
        }
        return response;
    }
}

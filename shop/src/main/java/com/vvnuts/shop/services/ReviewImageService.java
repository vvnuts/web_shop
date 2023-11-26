package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.repositories.ReviewImageRepository;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @Transactional
    public byte[] downloadImages(Integer imageId) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId).orElseThrow();
        return ImageUtils.decompressImage(reviewImage.getImage());
    }

    public void deleteImage(Integer imageId) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId).orElseThrow();
        reviewImageRepository.delete(reviewImage);
    }

    public List<Integer> convertEntityToListInteger(List<ReviewImage> reviewImages) {
        List<Integer> imagesId = new ArrayList<>();
        for (ReviewImage reviewImage: reviewImages) {
            imagesId.add(reviewImage.getId());
        }
        return imagesId;
    }
}

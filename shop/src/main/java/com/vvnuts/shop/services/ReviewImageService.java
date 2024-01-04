package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.repositories.ReviewImageRepository;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.ImageUtils;
import jakarta.transaction.Transactional;
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
    private final ReviewImageRepository repository;

    public Review uploadImage(MultipartFile file, Integer reviewId) throws IOException {
        if (file.isEmpty()){
            throw new FileIsEmptyException("Файл пуст.");
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
        return reviewRepository.save(review);
    }

    @Transactional
    public byte[] downloadImage(Integer imageId) {
        ReviewImage reviewImage = repository.findById(imageId).orElseThrow();
        if (reviewImage.getImage() == null) {
            return null;
        }
        return ImageUtils.decompressImage(reviewImage.getImage());
    }

    public void deleteImage(Integer imageId) {
        ReviewImage reviewImage = repository.findById(imageId).orElseThrow();
        repository.delete(reviewImage);
    }
}

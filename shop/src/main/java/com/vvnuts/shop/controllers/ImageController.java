package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.responses.ReviewImageResponse;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.services.CategoryService;
import com.vvnuts.shop.services.ReviewImageService;
import com.vvnuts.shop.services.ReviewService;
import com.vvnuts.shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ReviewImageService reviewImageService;

    @PostMapping("/user/{id}")
    public ResponseEntity<HttpStatus> uploadUserImage(@PathVariable Integer userId,
                                                      @RequestParam("image")MultipartFile file) throws IOException {
        userService.uploadImage(file, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> downloadUserImage(@PathVariable Integer userId){
        byte[] image =  userService.downloadImage(userId);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/category/{id}")
    public ResponseEntity<HttpStatus> uploadCategoryImage(@PathVariable Integer categoryId,
                                                          @RequestParam("image")MultipartFile file) throws IOException {
        categoryService.uploadImage(file, categoryId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> downloadCategoryImage(@PathVariable Integer categoryId) {
        byte[] image =  categoryService.downloadImage(categoryId);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<HttpStatus> uploadReviewImage(@PathVariable Integer reviewId,
                                                        @RequestParam("image")MultipartFile file) throws IOException {
        reviewImageService.uploadImage(file, reviewId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("review/{id}")
    public ResponseEntity<List<ReviewImageResponse>> downloadReviewImage(@PathVariable Integer reviewId) {
        List<ReviewImageResponse> images =  reviewImageService.downloadImages(reviewId);
        return ResponseEntity.ok(images);
    }
}

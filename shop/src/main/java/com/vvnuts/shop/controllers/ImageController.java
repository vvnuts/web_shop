package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.responses.ReviewImageResponse;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.services.*;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ReviewImageService reviewImageService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<HttpStatus> uploadUserImage(@PathVariable @Min(0) Integer userId,
                                                      @RequestParam("image")MultipartFile file) throws IOException {
        userService.uploadImage(file, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> downloadUserImage(@PathVariable @Min(0) Integer userId){
        byte[] image =  userService.downloadImage(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<HttpStatus> uploadItemImage(@PathVariable @Min(0) Integer itemId,
                                                      @RequestParam("image")MultipartFile file) throws IOException {
        userService.uploadImage(file, itemId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> downloadItemImage(@PathVariable @Min(0) Integer itemId){
        byte[] image =  userService.downloadImage(itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<HttpStatus> uploadCategoryImage(@PathVariable @Min(0) Integer categoryId,
                                                          @RequestParam("image")MultipartFile file) throws IOException {
        categoryService.uploadImage(file, categoryId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> downloadCategoryImage(@PathVariable @Min(0) Integer categoryId) {
        byte[] image =  categoryService.downloadImage(categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @PostMapping("/review/{reviewId}")
    public ResponseEntity<HttpStatus> uploadReviewImage(@PathVariable @Min(0) Integer reviewId,
                                                        @RequestParam("image")MultipartFile file) throws IOException {
        reviewImageService.uploadImage(file, reviewId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("review/{reviewId}")
    public ResponseEntity<List<ReviewImageResponse>> downloadReviewImage(@PathVariable @Min(0) Integer reviewId) {
        List<ReviewImageResponse> images =  reviewImageService.downloadImages(reviewId);
        return ResponseEntity.ok(images);
    }
}

package com.vvnuts.shop.controllers;

import com.vvnuts.shop.services.*;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
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

    @DeleteMapping("user/{userId}")
    public ResponseEntity<?> deleteUserImage(@PathVariable @Min(0) Integer userId) {
        userService.deleteImage(userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<HttpStatus> uploadItemImage(@PathVariable @Min(0) Integer itemId,
                                                      @RequestParam("image")MultipartFile file) throws IOException {
        itemService.uploadImage(file, itemId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> downloadItemImage(@PathVariable @Min(0) Integer itemId){
        byte[] image =  itemService.downloadImage(itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @DeleteMapping("item/{itemId}")
    public ResponseEntity<?> deleteItemImage(@PathVariable @Min(0) Integer itemId) {
        itemService.deleteImage(itemId);
        return ResponseEntity.ok(HttpStatus.OK);
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

    @DeleteMapping("category/{categoryId}")
    public ResponseEntity<?> deleteCategoryImage(@PathVariable @Min(0) Integer categoryId) {
        categoryService.deleteImage(categoryId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/review/{reviewId}")
    public ResponseEntity<HttpStatus> uploadReviewImage(@PathVariable @Min(0) Integer reviewId,
                                                        @RequestParam("image")MultipartFile file) throws IOException {
        reviewImageService.uploadImage(file, reviewId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("review/{reviewImageId}")
    public ResponseEntity<?> downloadReviewImage(@PathVariable @Min(0) Integer reviewImageId) {
        byte[] image =  reviewImageService.downloadImages(reviewImageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @DeleteMapping("review/{reviewImageId}")
    public ResponseEntity<?> deleteReviewImage(@PathVariable @Min(0) Integer reviewImageId) {
        reviewImageService.deleteImage(reviewImageId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

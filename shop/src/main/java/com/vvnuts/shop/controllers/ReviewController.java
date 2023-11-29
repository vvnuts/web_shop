package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.services.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController{
    private final ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ReviewRequest request){
        reviewService.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Collection<ReviewResponse>> findAll(){
        List<ReviewResponse> responses = reviewService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> findOne(@PathVariable @Min(0) Integer id) {
        ReviewResponse response = reviewService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid ReviewUpdateRequest request) {
        reviewService.update(request, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        reviewService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

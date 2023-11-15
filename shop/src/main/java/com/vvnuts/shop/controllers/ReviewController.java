package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController{
    private final ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody ReviewRequest dtoEntity){
        reviewService.create(dtoEntity);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Review>> findAll(){
        List<Review> entities = reviewService.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> findOne(@PathVariable Integer id) {
        Review entity = reviewService.findById(id);
        if(entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Integer id,
                                             @RequestBody ReviewUpdateRequest dtoEntity) {
        reviewService.update(dtoEntity, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        Review entity = reviewService.findById(id);
        reviewService.delete(entity);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

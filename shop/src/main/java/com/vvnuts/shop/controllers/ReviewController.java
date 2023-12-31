package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.exceptions.ReviewValidException;
import com.vvnuts.shop.services.ReviewService;
import com.vvnuts.shop.utils.validators.ReviewValidator;
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
    private final ReviewService service;
    private final ReviewValidator validator;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ReviewRequest request){
        validator.validate(request);
        service.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Collection<ReviewResponse>> findAll(){
        List<ReviewResponse> responses = service.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> findOne(@PathVariable @Min(0) Integer id) {
        ReviewResponse response = service.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid ReviewUpdateRequest request) {
        service.update(request, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(ReviewValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onReviewValidException(ReviewValidException e) {
        return e.getValidationErrorResponses();
    }
}

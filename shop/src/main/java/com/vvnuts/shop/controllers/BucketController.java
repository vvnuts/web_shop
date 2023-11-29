package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.services.BucketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bucket")
public class BucketController {
    private final BucketService bucketService;

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid BucketRequest request) {
        bucketService.create(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BucketResponse> findOne(@PathVariable @Min(0) Integer userId) {
        BucketResponse response = bucketService.findOne(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer userId,
                                             @RequestBody @Valid BucketRequest bucketRequest) {
        bucketService.update(bucketRequest, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

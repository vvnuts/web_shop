package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.services.BucketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/bucket")
public class BucketController {
    private final BucketService bucketService;

    @GetMapping("/{id}")
    public ResponseEntity<BucketResponse> findOne(@PathVariable @Min(0) Integer id) {
        Bucket bucket = bucketService.findBucketByUserId(id);
        if (bucket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bucketService.convertEntityToResponse(bucket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid BucketRequest bucketRequest) {

        bucketService.update(bucketRequest, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

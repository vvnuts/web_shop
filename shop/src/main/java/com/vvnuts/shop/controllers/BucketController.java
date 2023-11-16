package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.services.implementation.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bucket")
public class BucketController {
    private final BucketService bucketService;
 //TODO проверить как считается totalPrice
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody BucketRequest bucketRequest){
        bucketService.create(bucketRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BucketResponse> findOne(@PathVariable Integer id) {
        Bucket bucket = bucketService.findBucketByUserId(id);
        if (bucket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bucketService.convertEntityToResponse(bucket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Integer id,
                                             @RequestBody BucketRequest bucketRequest) {

        bucketService.update(bucketRequest, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

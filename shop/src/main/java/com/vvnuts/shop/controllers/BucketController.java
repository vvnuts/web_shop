package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.BucketDTO;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.services.interfaces.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bucket")
public class BucketController {
    private final BucketService bucketService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody BucketDTO dtoEntity){
        bucketService.create(dtoEntity);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bucket> findOne(@PathVariable Integer id) {
        Bucket bucket = bucketService.findBucketByUserId(id);
        if(bucket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bucket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Integer id,
                                             @RequestBody BucketDTO dtoEntity) {

        bucketService.update(dtoEntity, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

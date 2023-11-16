package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.services.implementation.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/characteristic")
public class CharacteristicController{
    private final CharacteristicService characteristicService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody CharacteristicRequest request){
        characteristicService.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

//    @GetMapping()
//    public ResponseEntity<List<E>> findAll(){
//        List<E> entities = characteristicService.findAll();
//        return ResponseEntity.ok(entities);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<E> findOne(@PathVariable Integer id) {
//        E entity = characteristicService.findById(id);
//        return ResponseEntity.ok(entity);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Integer id,
//                                             @RequestBody CharacteristicRequest request) {
//        characteristicService.update(request, id);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
//        E entity = characteristicService.findById(id);
//        characteristicService.delete(entity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
}

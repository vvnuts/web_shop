package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.services.CharacteristicService;
import com.vvnuts.shop.utils.validators.CharacteristicValidator;
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
@RequestMapping("/api/v1/characteristic")
public class CharacteristicController{
    private final CharacteristicService service;
    private final CharacteristicValidator validator;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CharacteristicRequest request){
        validator.validate(request);
        service.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Collection<CharacteristicResponse>> findAll(){
        List<CharacteristicResponse> responses = service.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacteristicResponse> findOne(@PathVariable @Min(0) Integer id) {
        CharacteristicResponse response = service.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid CharacteristicRequest request) {
        Characteristic characteristic = validator.validate(request, id);
        service.update(request, characteristic);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

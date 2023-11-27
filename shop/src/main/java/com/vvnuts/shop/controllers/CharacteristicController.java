package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.services.CharacteristicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/characteristic")
public class CharacteristicController{
    private final CharacteristicService characteristicService;

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CharacteristicRequest request){
        characteristicService.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CharacteristicResponse>> findAll(){
        List<CharacteristicResponse> responses = characteristicService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacteristicResponse> findOne(@PathVariable @Min(0) Integer id) {
        CharacteristicResponse response = characteristicService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable @Min(0) Integer id,
                                             @RequestBody @Valid CharacteristicRequest request) {
        characteristicService.update(request, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        characteristicService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

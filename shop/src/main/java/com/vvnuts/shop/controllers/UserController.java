package com.vvnuts.shop.controllers;

import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.entities.enums.Role;
import com.vvnuts.shop.services.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable @Min(0) Integer id) {
        User user = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> setRole(@PathVariable @Min(0) Integer id,
                                              @RequestParam("role")Role role) {
        service.setRole(id, role);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

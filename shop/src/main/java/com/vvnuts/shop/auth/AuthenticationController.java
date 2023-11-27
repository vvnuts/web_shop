package com.vvnuts.shop.auth;

import com.vvnuts.shop.auth.dtos.AuthenticationRequest;
import com.vvnuts.shop.auth.dtos.AuthenticationResponse;
import com.vvnuts.shop.auth.dtos.RegisterRequest;
import com.vvnuts.shop.auth.exceptions.AuthError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @ExceptionHandler
    private ResponseEntity<AuthError> handleException(AuthenticationException e){
        AuthError authError = new AuthError("Неправильный логин или пароль.");
        return new ResponseEntity<>(authError, HttpStatus.UNAUTHORIZED);
    }
}

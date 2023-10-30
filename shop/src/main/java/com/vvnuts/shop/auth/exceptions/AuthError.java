package com.vvnuts.shop.auth.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthError {
    private String message;
}
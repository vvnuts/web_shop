package com.vvnuts.shop.controllers;

import com.vvnuts.shop.exceptions.*;
import com.vvnuts.shop.dtos.responses.NotFoundResponse;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    NotFoundResponse onNoSuchElementException(NoSuchElementException e) {
        return NotFoundResponse.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({
            BucketValidException.class,
            NotFoundRelatedObjectException.class,
            CharacterItemValidException.class,
            OrderItemValidException.class,
            OrderValidException.class,
            ReviewValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onValidException(ValidException e) {
        return e.getValidationErrorResponses();
    }

    @ExceptionHandler({
            CategoryParentContainsItselfException.class,
            CycleHasFormedException.class,
            FileIsEmptyException.class,
            OrderStatusException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onRuntimeException(RuntimeException e) {
        return e.getMessage();
    }
}

package com.vvnuts.shop.exceptions;

public class OrderStatusException extends RuntimeException{
    public OrderStatusException(String message) {
        super(message);
    }
}
